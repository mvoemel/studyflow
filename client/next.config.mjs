/** @type {import('next').NextConfig} */
const nextConfig = {
  // Recommended: this will reduce output Docker image size by 80%+
  output: "standalone",
  async rewrites() {
    return [
      {
        source: "/api/:path*",
        destination: "http://localhost:8080/api/:path*",
      },
    ];
  },
};

export default nextConfig;
