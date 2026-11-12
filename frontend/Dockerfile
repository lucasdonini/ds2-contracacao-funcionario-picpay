FROM node:22-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm ci
COPY index.html vite.config.js ./
COPY src/ src/
ENV VITE_API_URL=/api/funcionarios
RUN npm run build

FROM nginxinc/nginx-unprivileged:stable-alpine
ENV BACKEND_URL=http://backend:8080
COPY nginx.conf.template /etc/nginx/templates/default.conf.template
COPY --from=build /app/dist/ /usr/share/nginx/html/
EXPOSE 8080
