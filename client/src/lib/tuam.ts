const BASE_URL = process.env.SERVER_BASE_URL || "/api";

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
      throw new Error(error);
    }

    return response.json() as Promise<TResponse>;
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
