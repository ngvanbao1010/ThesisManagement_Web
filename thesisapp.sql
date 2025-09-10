-- MySQL dump 10.13  Distrib 8.0.42, for Win64 (x86_64)
--
-- Host: localhost    Database: thesisapp
-- ------------------------------------------------------
-- Server version	8.0.42

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `committee`
--

DROP TABLE IF EXISTS `committee`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `committee` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `defense_date` datetime DEFAULT NULL,
  `available` tinyint(1) DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `committee`
--

LOCK TABLES `committee` WRITE;
/*!40000 ALTER TABLE `committee` DISABLE KEYS */;
INSERT INTO `committee` VALUES (1,'Hội đồng bảo vệ khóa luận','2025-08-10 09:00:00',0),(11,'Hội đồng số 4','2025-08-10 10:00:00',1),(12,'Hội đồng số 5','2025-08-10 10:00:00',1),(15,'Hội đồng số 5','2025-08-10 10:00:00',1),(16,'Hội đồng số 5','2025-08-10 10:00:00',1),(17,'Hội đồng số 5','2025-08-10 10:00:00',1),(18,'Hội đồng số 5','2025-08-10 10:00:00',1),(19,'Hội đồng số 5','2025-08-10 10:00:00',1),(20,'Hội đồng số 5','2025-08-10 10:00:00',0),(22,'Hội đồng số 7','2026-08-10 10:00:00',1),(23,'Hội đồng số 7','2026-08-10 10:00:00',0);
/*!40000 ALTER TABLE `committee` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `committee_members`
--

DROP TABLE IF EXISTS `committee_members`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `committee_members` (
  `committee_id` int NOT NULL,
  `lecturer_id` int NOT NULL,
  `role` enum('CHAIRMAN','SECRETARY','REVIEWER','MEMBER') NOT NULL,
  PRIMARY KEY (`committee_id`,`lecturer_id`),
  KEY `lecturer_id` (`lecturer_id`),
  CONSTRAINT `committee_members_ibfk_1` FOREIGN KEY (`committee_id`) REFERENCES `committee` (`id`),
  CONSTRAINT `committee_members_ibfk_2` FOREIGN KEY (`lecturer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `committee_members`
--

LOCK TABLES `committee_members` WRITE;
/*!40000 ALTER TABLE `committee_members` DISABLE KEYS */;
INSERT INTO `committee_members` VALUES (11,9,'CHAIRMAN'),(11,10,'SECRETARY'),(11,11,'REVIEWER'),(11,15,'MEMBER'),(12,9,'CHAIRMAN'),(12,10,'SECRETARY'),(12,15,'MEMBER'),(12,16,'REVIEWER'),(15,9,'CHAIRMAN'),(15,10,'SECRETARY'),(15,15,'MEMBER'),(15,16,'REVIEWER'),(16,9,'CHAIRMAN'),(16,10,'SECRETARY'),(16,15,'MEMBER'),(16,16,'REVIEWER'),(17,9,'CHAIRMAN'),(17,10,'SECRETARY'),(17,15,'MEMBER'),(17,16,'REVIEWER'),(18,9,'CHAIRMAN'),(18,10,'SECRETARY'),(18,15,'MEMBER'),(18,16,'REVIEWER'),(19,9,'CHAIRMAN'),(19,10,'SECRETARY'),(19,15,'MEMBER'),(19,16,'REVIEWER'),(20,9,'CHAIRMAN'),(20,10,'SECRETARY'),(20,15,'MEMBER'),(20,16,'REVIEWER'),(22,9,'CHAIRMAN'),(22,10,'SECRETARY'),(22,15,'MEMBER'),(22,16,'REVIEWER'),(23,9,'CHAIRMAN'),(23,10,'SECRETARY'),(23,15,'MEMBER'),(23,16,'REVIEWER');
/*!40000 ALTER TABLE `committee_members` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_criteria`
--

DROP TABLE IF EXISTS `evaluation_criteria`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_criteria` (
  `id` int NOT NULL AUTO_INCREMENT,
  `criterion` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_criteria`
--

LOCK TABLES `evaluation_criteria` WRITE;
/*!40000 ALTER TABLE `evaluation_criteria` DISABLE KEYS */;
INSERT INTO `evaluation_criteria` VALUES (1,'Khả năng trình bày và phản biện'),(2,'Viết lách'),(3,'Chuyên môn'),(4,'Tài liệu');
/*!40000 ALTER TABLE `evaluation_criteria` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `evaluation_detail`
--

DROP TABLE IF EXISTS `evaluation_detail`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `evaluation_detail` (
  `id` int NOT NULL AUTO_INCREMENT,
  `thesis_id` int NOT NULL,
  `lecturer_id` int NOT NULL,
  `criterion_id` int NOT NULL,
  `score` double DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `thesis_id` (`thesis_id`),
  KEY `lecturer_id` (`lecturer_id`),
  KEY `criterion_id` (`criterion_id`),
  CONSTRAINT `evaluation_detail_ibfk_1` FOREIGN KEY (`thesis_id`) REFERENCES `thesis` (`id`),
  CONSTRAINT `evaluation_detail_ibfk_2` FOREIGN KEY (`lecturer_id`) REFERENCES `user` (`id`),
  CONSTRAINT `evaluation_detail_ibfk_3` FOREIGN KEY (`criterion_id`) REFERENCES `evaluation_criteria` (`id`),
  CONSTRAINT `evaluation_detail_chk_1` CHECK ((`score` between 0 and 10))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `evaluation_detail`
--

LOCK TABLES `evaluation_detail` WRITE;
/*!40000 ALTER TABLE `evaluation_detail` DISABLE KEYS */;
INSERT INTO `evaluation_detail` VALUES (1,1,9,1,8.5),(2,1,9,1,8.5),(3,2,10,2,7.5),(4,2,10,3,8),(5,3,9,3,8.2),(6,3,9,4,7.7);
/*!40000 ALTER TABLE `evaluation_detail` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thesis`
--

DROP TABLE IF EXISTS `thesis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thesis` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `created_date` datetime DEFAULT CURRENT_TIMESTAMP,
  `status` enum('PENDING','DEFENDED','CLOSED') DEFAULT NULL,
  `file_khoaluan` varchar(255) DEFAULT NULL,
  `committee_id` int DEFAULT NULL,
  `major` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_thesis_committee` (`committee_id`),
  CONSTRAINT `fk_thesis_committee` FOREIGN KEY (`committee_id`) REFERENCES `committee` (`id`),
  CONSTRAINT `chk_major` CHECK ((`major` in (_utf8mb4'IT',_utf8mb4'LOGISTICS',_utf8mb4'ACCOUNTING',_utf8mb4'AUDIT',_utf8mb4'COMPUTER_SCIENCE',_utf8mb4'LAW',_utf8mb4'MARKETING',_utf8mb4'MEDICINE',_utf8mb4'NURSING',_utf8mb4'INTERIOR_DESIGN')))
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thesis`
--

LOCK TABLES `thesis` WRITE;
/*!40000 ALTER TABLE `thesis` DISABLE KEYS */;
INSERT INTO `thesis` VALUES (1,'Nhận dạng khuôn mặt','2025-08-02 00:00:00','CLOSED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754123582/xxhuxsvgywocqlhhka0h',20,'IT'),(2,'Thùng rác thông minh','2025-08-02 00:00:00','CLOSED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754128255/igljtrnjjxecsdvuzgm1',23,'AUDIT'),(3,'Nhà thông minh','2025-08-02 00:00:00','CLOSED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754128737/ypdgqtf8na1ut21392es',23,'AUDIT'),(4,'Tưới cây tự động','2025-08-02 00:00:00','DEFENDED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754128255/igljtrnjjxecsdvuzgm1',NULL,'LAW'),(5,'Bãi giữ xe thông minh','2025-08-09 00:00:00','DEFENDED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754128255/igljtrnjjxecsdvuzgm1',NULL,'AUDIT'),(6,'Phòng khám đa khoa','2025-08-09 00:00:00','DEFENDED','https://res.cloudinary.com/dxxwcby8l/raw/upload/v1754128255/igljtrnjjxecsdvuzgm1',NULL,'IT');
/*!40000 ALTER TABLE `thesis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thesis_lecturers`
--

DROP TABLE IF EXISTS `thesis_lecturers`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thesis_lecturers` (
  `thesis_id` int NOT NULL,
  `lecturer_id` int NOT NULL,
  PRIMARY KEY (`thesis_id`,`lecturer_id`),
  KEY `lecturer_id` (`lecturer_id`),
  CONSTRAINT `thesis_lecturers_ibfk_1` FOREIGN KEY (`thesis_id`) REFERENCES `thesis` (`id`),
  CONSTRAINT `thesis_lecturers_ibfk_2` FOREIGN KEY (`lecturer_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thesis_lecturers`
--

LOCK TABLES `thesis_lecturers` WRITE;
/*!40000 ALTER TABLE `thesis_lecturers` DISABLE KEYS */;
INSERT INTO `thesis_lecturers` VALUES (3,9),(2,10),(5,10),(4,11),(5,11),(6,16);
/*!40000 ALTER TABLE `thesis_lecturers` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `thesis_students`
--

DROP TABLE IF EXISTS `thesis_students`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `thesis_students` (
  `thesis_id` int NOT NULL,
  `student_id` int NOT NULL,
  PRIMARY KEY (`thesis_id`,`student_id`),
  KEY `student_id` (`student_id`),
  CONSTRAINT `thesis_students_ibfk_1` FOREIGN KEY (`thesis_id`) REFERENCES `thesis` (`id`),
  CONSTRAINT `thesis_students_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `thesis_students`
--

LOCK TABLES `thesis_students` WRITE;
/*!40000 ALTER TABLE `thesis_students` DISABLE KEYS */;
INSERT INTO `thesis_students` VALUES (1,8),(3,13),(2,14),(2,17),(6,21),(5,22),(4,23),(5,24);
/*!40000 ALTER TABLE `thesis_students` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `user` (
  `id` int NOT NULL AUTO_INCREMENT,
  `first_name` varchar(100) NOT NULL,
  `last_name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(20) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(255) NOT NULL,
  `active` tinyint(1) DEFAULT '1',
  `user_role` enum('STUDENT','LECTURER','ACADEMIC_STAFF','ADMIN') DEFAULT NULL,
  `avatar` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (6,'Mai Chí','Công','admin@gmail.com','0399646679','admin1','$2a$10$/pXW0UO/WE7EE1JJ65kZdufCLcCiicwBRfuUvxGAuPr72Dw9jUtP.',1,'ADMIN','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754111896/e4xqqur7wok0eefm42ng.png'),(7,'Nguyễn Thị','Mai','admin@gmail.com','0399646679','admin','$2a$10$B46lhrXwPR9WpRiUf5NA.ulsGCfXon53YLuY8KBVpuP06jdp63UfO',1,'ADMIN','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754111982/zxahd9sx9m8ly2batvrn.png'),(8,'Lê thị','Huệ','2251052062loi@ou.edu.vn','039964624','student1','$2a$10$01218Zn2yYO3sXfXWMJDfedWzeizngCY5E/ztaqMTfkEjgslckKC.',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754114273/fx9u9i6vhaefd67t0cos.png'),(9,'Trần Văn','Lợi','student1@gmail.com','039964624','lecturer1','$2a$10$CxnnfT8S68.GjdFRFvKFBe3uEdE5EFwVpqn/bSSUlKE2JoLiDKUyO',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754128727/sdsirck82z6x0u5hjh9y.png'),(10,'Trúc Tiểu','Hồng','student1@gmail.com','039964624','lecturer2','$2a$10$RnvNCFKvXSRPI5.DK303/urcxg3Zjs8MVZENsts/MOzBvgcXLQdr6',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754139358/ska0avlxx0chkz01toy5.png'),(11,'Trần Thị','Hồng','student1@gmail.com','039964624','lecturer3','$2a$10$LkIAnAjg/m8IPC6EqX5yuuyq0fwZ21X5SSwAhVDpfFmf/.W2H7eKS',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754139363/zb11z3upazuvdqavg2f3.png'),(12,'Đinh Chí','Hào','student1@gmail.com','039964624','staff1','$2a$10$jfdOypFzmRhAQ6K6kgXzS.p1kkh7Lnu/T6p665.MD6Rhfbl2SJOae',1,'ACADEMIC_STAFF','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754139511/kkpkh2zttk4gidiuiulu.png'),(13,'Bùi Mĩ','Hạnh','s83sky83s@gmail.com','039964624','student2','$2a$10$zrQYyXXy1lQp4Ohf.eLkquIZL6TqjntkR5RDy3ETCtqI1dT/.7a9S',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754139759/i04xirrxumiup3cjxrxx.png'),(14,'Đặng Hoàng','Hà','tamoki0tv@gmail.com','039964624','student3','$2a$10$NjCPqpAakd.D8zSYDFdWROK.KOPXFqoxCeH4KFDkLfrcyXTl6IPXi',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754186915/tqzqswno77kslkgf6nda.png'),(15,'Trần Văn','Hoàng','student1@gmail.com','039863846','lecturer4','$2a$10$wbeq8heKykbSr2LSLg.v1.x5PRCd5Yk8GCVQCcvtmSTh2XGCd0CMq',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754205120/opbhaqyi5nlbf4nfliul.png'),(16,'Trần Công','Hậu','s83sky83s@gmail.com','039863846','lecturer5','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(17,'Bùi Khánh','Đăng','326kjo@gmail.com','099262678','student4','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(18,'Thạch Hào','Quốc','quoc055@gmail.com','099262678','lecturer6','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(19,'Đào Mộng','Yên','yen099@gmail.com','099262678','lecturer7','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(20,'Tuần Nguyên','Phong','phonghoang@gmail.com','099262678','lecturer8','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'LECTURER','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(21,'Hoàng Văn','Khoa','khoa33@gmail.com','099262678','student5','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(22,'Nguyễn Thành','Đạt','datkk32@gmail.com','099262678','student6','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(23,'Chung Tử','Huyên','huyentrang@gmail.com','099262678','student7','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png'),(24,'Nguyễn Văn','Bảo','baols@gmail.com','099262678','student8','$2a$10$X2ZiKrQ3BnVTCglUhdxkwufJL.zJKjEIatnE4JFY0n3Lqgyrcu2Am',1,'STUDENT','https://res.cloudinary.com/dxxwcby8l/image/upload/v1754211245/u20zkuxjze6jzztiejwg.png');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-08-09 11:10:47
