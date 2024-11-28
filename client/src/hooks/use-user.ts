import { useState, useEffect } from "react";

type UserToken = {
  username: string;
  firstname: string;
  lastname: string;
  iat: number;
  exp: number;
};

// TODO: make more efficient so that it not always sends a request
/**
 * This hook fetches the currently loged in user.
 *
 * @returns the currently logged in user
 * @returns loading state
 */
const useUser = () => {
  const [user, setUser] = useState<UserToken | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await fetch("/api/auth/me");
        if (response.ok) {
          const data = await response.json();
          setUser(data.user);
        }
      } catch (error) {
        console.error("Failed to fetch user:", error);
      } finally {
        setLoading(false);
      }
    };

    fetchUser();
  }, []);

  return { user, loading };
};

export { useUser };
