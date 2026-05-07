FROM node:18

WORKDIR /app

COPY package*.json ./

RUN npm install

COPY . .

EXPOSE 5173

# Используем правильный скрипт
CMD ["npm", "run", "dev"]