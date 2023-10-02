import express, { Response } from 'express';

import {
    config as configGoogleOAuth2,
    configRoutes as configGoogleOAuth2Routes,
} from './strategies/passport-google-oauth2.service.js';

import { config as configJWT } from './strategies/passport-jwt.service.js';
import { configSession } from './configurations/configSession.js';
import { configParsers } from './configurations/configParsers.js';
import { environment } from './configurations/environment.js';
import passport from 'passport';
import { Card, ConnectionCard, Tag, User } from '@prisma/client';
import { prisma } from './databases/userDatabase.js';
// Express App
const app = express();

// Session is required by passport.js
configSession(app);

// Parsing capabilities for body of request.
configParsers(app);

// https://www.passportjs.org/packages/passport-google-oauth2/
configGoogleOAuth2();

// https://www.passportjs.org/packages/passport-jwt/
configJWT();

// adding required routes for authentication for this strategy
configGoogleOAuth2Routes(app);

/**
 * Required by Android
 * https://www.branch.io/resources/blog/how-to-open-an-android-app-from-the-browser/
 * https://developer.android.com/training/app-links/verify-android-applinks
 */
app.get('/.well-known/assetlinks.json', (req: any, res: any) => {
    res.json([
        {
            relation: ['delegate_permission/common.handle_all_urls'],
            target: {
                namespace: 'android_app',
                package_name: 'com.sbma.linkup',
                sha256_cert_fingerprints: environment.APP_ANDROID_SHA256_CERT_FINGERPRINT.split(','),
            },
        },
    ]);
});

app.get('/profile', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const response = await prisma.user.findFirst({
        where: {
            id: (req.user as User).id,
        },
        include: {
            cards: true,
            connections: {
                include: {
                    connectionCards: true,
                },
            },
            connectedUsers: true,
            connects: {
                include: {
                    tags: true,
                    cards: true,
                },
            },
        },
    });
    res.status(200).send(response);
    return;
});

app.put('/profile', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const body: User = req.body as User;
    const response = await prisma.user.update({
        where: {
            id: user.id,
        },
        data: {
            ...user,
            ...body,
            id: user.id,
        },
    });

    res.status(200).send(response);
    return;
});

app.post('/card', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const body: Card = req.body as Card;
    const response = await prisma.card.create({
        data: {
            ownerId: user.id,
            title: body.title,
            value: body.value,
        },
    });
    res.status(200).send(response);
    return;
});

app.post('/share', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const body: string[] = req.body as string[];

    const connect = await prisma.connect.create({
        data: {
            userId: user.id,
        },
    });
    const connectCards = await Promise.all(
        body.map(async (cardId) => {
            // Only user's own card
            await prisma.card.findFirstOrThrow({
                where: {
                    id: cardId,
                    ownerId: user.id,
                },
            });
            return prisma.connectCard.create({
                data: {
                    cardId: cardId,
                    connectId: connect.id,
                },
            });
        }),
    );

    res.status(200).send({
        connect,
        connectCards,
    });
    return;
});

async function onScan(user: User, connectId: string, res: Response) {
    const connect = await prisma.connect.findFirstOrThrow({
        where: {
            id: connectId,
        },
        include: {
            tags: true,
            cards: true,
        },
    });
    const connectCards = await prisma.connectCard.findMany({
        where: {
            connectId: connect.id,
        },
    });
    if (connect.userId === user.id) {
        res.status(400).send({
            message:
                "Looks like you've fallen into a QR code loop! We'll break the cycle for you - scan someone else's code to escape the matrix!",
        });
        return;
    }
    const existingConnection = await prisma.connection.findFirst({
        where: {
            userId: connect.userId,
            connectedUserId: user.id,
        },
    });
    const connection =
        existingConnection ??
        (await prisma.connection.create({
            data: {
                userId: connect.userId,
                connectedUserId: user.id,
            },
        }));

    const connectionCards = await Promise.all(
        connectCards.map(async (connectCard) => {
            const card: ConnectionCard | null = await prisma.connectionCard.findFirst({
                where: {
                    cardId: connectCard.cardId,
                    connectionId: connection.id,
                },
            });
            return card
                ? new Promise<ConnectionCard>((resolve) => resolve(card))
                : prisma.connectionCard.create({
                      data: {
                          cardId: connectCard.cardId,
                          connectionId: connection.id,
                      },
                  });
        }),
    );
    res.status(200).send({
        connection,
        connectionCards,
    });
}

app.post('/connect/:id/scan', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const connectId = req.params.id;
    await onScan(user, connectId, res);
});

app.post('/tag', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const body: Tag = req.body as Tag;
    const response = await prisma.tag.create({
        data: {
            connectId: body.connectId,
            tagId: body.tagId,
        },
    });
    res.status(200).send(response);
    return;
});
app.post('/tag/:id/scan', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const tagId = req.params.id;

    const response = await prisma.tag.findFirstOrThrow({
        where: {
            tagId,
        },
    });
    await onScan(user, response.connectId, res);
});

app.delete('/tag/:id', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const id = req.params.id;
    const body: Tag = req.body as Tag;

    const tag = await prisma.tag.findFirstOrThrow({
        where: {
            id: id,
        },
        include: {
            connect: true,
        },
    });
    if (tag.connect.userId !== user.id) {
        res.status(400).send({
            message: 'This tag is not available.',
        });
        return;
    }

    const response = await prisma.tag.delete({
        where: {
            id,
        },
    });
    res.status(200).send(response);
    return;
});

app.listen(8000, '0.0.0.0', () => {
    console.log('Started listening on port 8000');
});
