import { GoogleProfile } from './googleProfile.js';

export interface UserModel {
    id: string;
    name: string;
    description: string;
    googleProfile: GoogleProfile;
}
