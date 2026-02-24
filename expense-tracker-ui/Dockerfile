# Dockerfile dans expense-tracker-ui
FROM node:18 AS build
WORKDIR /app
COPY . /app
RUN npm install && npm run build

FROM nginx:alpine
COPY --from=build /app/dist/expense-tracker-ui/browser /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]