import { randomUUID } from 'crypto';
import { UserModel } from '../interfaces/userModel.js';
import { GoogleProfile } from '../interfaces/googleProfile.js';
import { PrismaClient } from '@prisma/client';

const prisma = new PrismaClient();
prisma.user.findMany().then((data) => {
    console.log(data);
});
const users: Map<string, UserModel> = new Map();

function syncUserByGoogleProfile(googleProfile: GoogleProfile) {
    const user = findByEmail(googleProfile.email);

    if (!user) {
        return createUser(googleProfile);
    }
    const updatedUserModel: UserModel = {
        id: user.id,
        name: user.name,
        description: user.description,
        googleProfile,
    };
    users.set(user.id, updatedUserModel);
    return updatedUserModel;
}

function findByEmail(email: string) {
    const userEntries = [...users.entries()];
    const result =
        userEntries.find(
            ([userId, userModel]) => userModel.googleProfile.email.toLowerCase().trim() === email.toLowerCase().trim(),
        ) ?? null;

    return result ? result[1] : null;
}

function findById(id: string) {
    return users.get(id) ?? null;
}

function createUser(googleProfile: GoogleProfile) {
    const id = randomUUID();
    const userModel: UserModel = {
        id,
        name: googleProfile.displayName,
        description: '',
        googleProfile,
    };

    users.set(id, userModel);
    return userModel;
}

export const UserDatabase = {
    createUser,
    findByEmail,
    findById,
    syncUserByGoogleProfile: syncUserByGoogleProfile,
};
