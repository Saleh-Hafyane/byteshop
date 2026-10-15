// What the app remembers after login, stored in localStorage as "token" / "username".
export interface User {
  token: string | null; // the JWT, null when logged out
  username: string | null; // who is logged in, shown in the header
  role: string | null; // "USER" or "ADMIN", drives menu visibility
}
