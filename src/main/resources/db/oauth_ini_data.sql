INSERT INTO `oauth_account` VALUES (1, 'ABC', 'ealenxie', '$2a$10$IzjmkjegAMXtycRnGyBZl.ZMwNxoUhCCCn8/lwlLswdMQ6TcvU3P2', '1232378743', 'abc@123.com', 1, 1, 1, 1, 0, '2020-12-07 11:06:02');

INSERT INTO `oauth_client_details`(`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('ABC', 'demo-app', '$2a$10$LaY9MNGFaInbMTx1nhaVXuGwyqMmNExCYoGZK/FJL2G91SIfVnXp2', 'read,write', 'client_credentials,authorization_code,password,refresh_token,implicit', 'http://www.baidu.com', 'user', 7199, 2592000, NULL, 'true');

