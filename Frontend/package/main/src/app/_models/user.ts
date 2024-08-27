export class User {
  id?: number;
  username?: string;
  password?: string;
  email?: string;
  authorities?: string[];
  accessToken?: string;
  refreshToken?: string;
}
