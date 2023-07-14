FROM node:16.13.0-alpine
WORKDIR /usr/app
COPY . /usr/app
RUN npm install
RUN npm run-script build

FROM nginx:latest
COPY ./nginx/prod_server /etc/nginx/conf.d/default.conf
COPY --from=0 /usr/app/build /usr/share/nginx/html