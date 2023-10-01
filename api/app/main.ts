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
import { randomUUID } from 'crypto';
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
                sha256_cert_fingerprints: [environment.APP_ANDROID_SHA256_CERT_FINGERPRINT],
            },
        },
    ]);
});

const data = new Map();

app.post('/', passport.authenticate('jwt', { session: false }), (req, res) => {
    const id = randomUUID();
    const { body } = req;
    data.set(id, body ?? {});
    res.send({ id: id });
});

app.get('/:id', passport.authenticate('jwt', { session: false }), (req, res) => {
    const { user } = req;
    console.log(user);
    const { id } = req.params;
    res.send({ data: data.get(id) ?? null });
});

app.listen(8000, '0.0.0.0', () => {
    console.log('Started listening on port 8000');
});
