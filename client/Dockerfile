FROM node:current-alpine
WORKDIR /usr/app
COPY . /usr/app
RUN npm install
RUN npm run-script build

FROM nginx:latest
COPY --from=0 /usr/app/build /usr/share/nginx/html
