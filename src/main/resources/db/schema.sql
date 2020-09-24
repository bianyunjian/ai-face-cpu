CREATE SCHEMA `ai_server_face_cpu` DEFAULT CHARACTER SET utf8mb4 ;



CREATE TABLE `face` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `face_library_id` int(11) NOT NULL,
                        `person_name` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
                        `image_url` varchar(512) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                        `features` blob ,
                        PRIMARY KEY (`id`),
                        KEY `cluster_key_face_library_id` (`face_library_id`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE `face_library` (
                                `id` int(11) NOT NULL AUTO_INCREMENT,
                                `product_id` int(11) NOT NULL,
                                `name` varchar(75) COLLATE utf8mb4_unicode_ci NOT NULL,
                                `description` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `UNIQUE_product_id_and_name` (`name`,`product_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10000001 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci