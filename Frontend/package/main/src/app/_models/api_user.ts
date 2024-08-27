export interface ApiUser {
  id?: number;
  username?: string;
  email?: string;
  creationDate?: Date;
  lastSeen?: Date;
  superUser?: boolean;
  systemRoles?: Array<String>;
  companyRoles?: Object;
}
