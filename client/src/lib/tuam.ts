const BASE_URL = process.env.NEXT_PUBLIC_SERVER_BASE_URL || "";

/**
 * This object allows you to make type safe requests
 * to a specified route with the base url SERVER_BASE_URL,
 * kinda like the axios package.
 * It supports GET, POST, PATCH and DELETE methods.
 * Tuam is from latin and stands for "your request".
 */
const tuam = {
  async request<TResponse, TBody = unknown>(
    route: string,
    method: "GET" | "POST" | "PATCH" | "DELETE",
    body?: TBody,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    const url = `${BASE_URL}${route}`.replace(/\/+/g, "/");

    const response = await fetch(url, {
      method,
      headers: {
        "Content-Type": "application/json",
        ...headers,
      },
      body: body ? JSON.stringify(body) : undefined,
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || "An error occurred");
    }

    try {
      return response.json() as Promise<TResponse>;
    } catch {
      return {} as TResponse;
    }
  },

  async get<TResponse>(
    route: string,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    return this.request<TResponse>(route, "GET", undefined, headers);
  },

  async post<TResponse, TBody = unknown>(
    route: string,
    body: TBody,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    return this.request<TResponse, TBody>(route, "POST", body, headers);
  },

  async patch<TResponse, TBody = unknown>(
    route: string,
    body: TBody,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    return this.request<TResponse, TBody>(route, "PATCH", body, headers);
  },

  async delete<TResponse>(
    route: string,
    headers?: Record<string, string>
  ): Promise<TResponse> {
    return this.request<TResponse>(route, "DELETE", undefined, headers);
  },
};

export { tuam };
