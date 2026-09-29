FROM node:20

WORKDIR /app

# Install dependencies first (better caching)
COPY package*.json ./
RUN npm install --legacy-peer-deps

# Copy the Angular source
COPY . .

EXPOSE 4200

# Dev server, reachable from Windows, with polling for file watching
CMD ["npm", "start", "--", "--host", "0.0.0.0", "--poll=2000"]
