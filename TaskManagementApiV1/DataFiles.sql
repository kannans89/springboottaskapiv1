-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               8.0.31 - MySQL Community Server - GPL
-- Server OS:                    Win64
-- HeidiSQL Version:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

-- Dumping structure for table springv1.task
CREATE TABLE IF NOT EXISTS `task` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `completed` bit(1) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `priority` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `category_id` bigint DEFAULT NULL,
  `user` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKkjb4pwpo8oqc8fvkgbmiitsu9` (`category_id`),
  KEY `FK8tnv1cug8b6wqhwvcj0wrw3sb` (`user`),
  CONSTRAINT `FK8tnv1cug8b6wqhwvcj0wrw3sb` FOREIGN KEY (`user`) REFERENCES `user` (`id`),
  CONSTRAINT `FKkjb4pwpo8oqc8fvkgbmiitsu9` FOREIGN KEY (`category_id`) REFERENCES `category` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table springv1.task: ~1 rows (approximately)
INSERT INTO `task` (`id`, `completed`, `description`, `priority`, `title`, `category_id`, `user`) VALUES
	(1, b'0', 'buy abc,buy xyz', 'high', 'shopping list', 1, 1);

-- Dumping structure for table springv1.user
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Dumping data for table springv1.user: ~1 rows (approximately)
INSERT INTO `user` (`id`, `email`, `name`, `password`) VALUES
	(1, 'task@abc.com', 'kannan sudhakaran task', 'abc');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
