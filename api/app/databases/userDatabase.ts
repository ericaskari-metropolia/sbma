import { GoogleProfile } from '../interfaces/googleProfile.js';
import { PrismaClient } from '@prisma/client';

export const prisma = new PrismaClient();

async function syncUserByGoogleProfile(googleProfile: GoogleProfile) {
    const user = await prisma.user.findFirst({
        where: {
            email: googleProfile.email,
        },
    });

    if (!user) {
        return prisma.user.create({
            data: {
                email: googleProfile.email,
                name: googleProfile.displayName,
            },
        });
    }

    return user;
}

export const UserDatabase = {
    syncUserByGoogleProfile: syncUserByGoogleProfile,
};
