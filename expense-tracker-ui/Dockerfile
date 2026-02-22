# Build stage
FROM node:18 AS build
WORKDIR /app
COPY expense-tracker-ui /app
RUN npm install && npm run build

# Runtime stage
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]