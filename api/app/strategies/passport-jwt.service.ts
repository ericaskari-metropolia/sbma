import passport, { AuthenticateCallback } from 'passport';

import { ExtractJwt, Strategy as JwtStrategy, StrategyOptions, VerifiedCallback } from 'passport-jwt';
import jsonwebtoken from 'jsonwebtoken';
import { prisma } from '../databases/userDatabase.js';
import { environment } from '../configurations/environment.js';
import * as express from 'express';
import { User } from '@prisma/client';

export function config() {
    const opts: StrategyOptions = {
        jwtFromRequest: ExtractJwt.fromAuthHeaderAsBearerToken(),
        secretOrKey: environment.APP_JWT_SECRET,
        issuer: environment.APP_JWT_ISSUER,
        audience: environment.APP_JWT_AUDIENCE,
        passReqToCallback: true,
    };
    passport.use(
        new JwtStrategy(opts, async function (req: express.Request, payload: any, done: VerifiedCallback) {
            console.log({ payload });
            const user = await prisma.user.findFirst({
                where: {
                    id: payload.sub,
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
            if (user) {
                return done(null, user);
            } else {
                return done(null, false);
                // or you could create a new account
            }
        }),
    );
}

export function generateAccessToken(user: User, expiresInSeconds = 3600) {
    const payload = { sub: user.id, type: 'USER_LOGIN_TOKEN' };

    const accessToken = jsonwebtoken.sign(payload, environment.APP_JWT_SECRET, {
        expiresIn: expiresInSeconds,
        audience: environment.APP_JWT_AUDIENCE,
        issuer: environment.APP_JWT_ISSUER,
    });

    const expiresAt = Date.now() + expiresInSeconds * 1000;

    return {
        accessToken,
        expiresAt,
    };
}
export const jwtAuthenticateDebugCallback: AuthenticateCallback = (err, user, info, status) => {
    console.log({ err, user, info, status });
};
