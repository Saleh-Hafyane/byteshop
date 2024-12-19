import { User } from './user';

describe('User', () => {
  it('should create an instance', () => {
    const mockUser: User = {
      token:'test-token',
      username:'test-username'
    };
    expect(mockUser).toBeTruthy();
  });
});
