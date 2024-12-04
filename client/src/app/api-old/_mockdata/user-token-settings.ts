import { Settings, TokenAttributes, UserWithoutPassword } from "@/types";

const mockToken: TokenAttributes = {
  userId: "user-1",
  email: "john.doe@example.com",
  settingsId: "settings-1",
};

const mockUser: UserWithoutPassword = {
  id: "user-1",
  email: "john.doe@example.com",
  firstname: "John",
  lastname: "Doe",
  settingsId: "settings-1",
};

const mockSettings: Settings = {
  id: "settings-1",
  globalCalendarId: "calendar-1",
  activeDegreeId: "degree-1",
};

export { mockToken, mockUser, mockSettings };
