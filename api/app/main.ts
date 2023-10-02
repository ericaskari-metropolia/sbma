import express from 'express';

import {
    config as configGoogleOAuth2,
    configRoutes as configGoogleOAuth2Routes,
} from './strategies/passport-google-oauth2.service.js';

import { config as configJWT } from './strategies/passport-jwt.service.js';
import { configSession } from './configurations/configSession.js';
import { configParsers } from './configurations/configParsers.js';
import { environment } from './configurations/environment.js';
import passport from 'passport';
import { Card, ConnectCard, User } from '@prisma/client';
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

app.post('/connect', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const response = await prisma.connect.create({
        data: {
            userId: user.id,
        },
    });
    res.status(200).send(response);
    return;
});
app.post('/connect-card', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const body: ConnectCard = req.body as ConnectCard;
    const card = await prisma.card.findFirstOrThrow({
        where: {
            id: body.cardId,
        },
    });
    const connect = await prisma.connect.findFirstOrThrow({
        where: {
            id: body.connectId,
        },
    });
    if (connect.userId !== user.id || card.ownerId !== user.id) {
        res.status(401).send();
        return;
    }

    const response = await prisma.connectCard.create({
        data: {
            cardId: body.cardId,
            connectId: body.connectId,
        },
    });

    res.status(200).send(response);
    return;
});

app.post('/connect/:connectId/scan', passport.authenticate('jwt', { session: false }), async (req, res) => {
    const user: User = req.user as User;
    const connectId = req.params.connectId;

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
            message: "Don't scan your code :)",
        });
        return;
    }
    const connection = await prisma.connection.create({
        data: {
            userId: connect.userId,
            connectedUserId: user.id,
        },
    });
    console.log({ connection, connectCards });

    const connectionCards = await Promise.all(
        connectCards.map((connectCard) =>
            prisma.connectionCard.create({
                data: {
                    cardId: connectCard.cardId,
                    connectionId: connection.id,
                },
            }),
        ),
    );
    res.status(200).send({
        connection,
        connectionCards,
    });
    return;
});

app.listen(8000, '0.0.0.0', () => {
    console.log('Started listening on port 8000');
});
