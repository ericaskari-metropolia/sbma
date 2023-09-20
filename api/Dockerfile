FROM node:hydrogen-alpine
COPY package.json package.json
COPY package-lock.json package-lock.json
RUN npm install
COPY . .
CMD ["main.js"]
