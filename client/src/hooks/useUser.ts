import { useState, useEffect } from "react";

type UserToken = {
  username: string;
  iat: number;
  exp: number;
};

const useUser = () => {
  const [user, setUser] = useState<UserToken | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchUser = async () => {
      try {
        const response = await fetch("/api/auth/user");
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
