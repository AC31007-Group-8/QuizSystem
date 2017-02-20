-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema quizsystem
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema quizsystem
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `quizsystem` DEFAULT CHARACTER SET utf8 ;
USE `quizsystem` ;

-- -----------------------------------------------------
-- Table `quizsystem`.`Staff`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Staff` (
  `staff_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NULL,
  `second_name` VARCHAR(45) NULL,
  `email` VARCHAR(45) NULL,
  `staff_number` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  PRIMARY KEY (`staff_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Quiz`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Quiz` (
  `publish_status` TINYINT(1) NULL,
  `staff_id` INT NOT NULL,
  `module_code` VARCHAR(15) NULL,
  `quiz_id` INT NOT NULL AUTO_INCREMENT,
  `time_limit` INT NULL,
  `module_id` VARCHAR(10) NULL,
  `Title` VARCHAR(45) NULL,
  INDEX `quiz_to_staff_idx` (`staff_id` ASC),
  PRIMARY KEY (`quiz_id`),
  CONSTRAINT `quiz_to_staff`
    FOREIGN KEY (`staff_id`)
    REFERENCES `quizsystem`.`Staff` (`staff_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Student` (
  `student_id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NULL,
  `password` VARCHAR(45) NULL,
  `matric_number` VARCHAR(45) NULL,
  PRIMARY KEY (`student_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Question`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Question` (
  `question_id` INT NOT NULL AUTO_INCREMENT,
  `question` VARCHAR(5000) NULL,
  `quiz_id` INT NULL,
  `explanation` VARCHAR(5000) NULL,
  PRIMARY KEY (`question_id`),
  INDEX `question_to_quiz_idx` (`quiz_id` ASC),
  CONSTRAINT `question_to_quiz`
    FOREIGN KEY (`quiz_id`)
    REFERENCES `quizsystem`.`Quiz` (`quiz_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Answer` (
  `answer` VARCHAR(100) NULL,
  `question_id` INT NULL,
  `is_correct` TINYINT(1) NULL,
  `answer_id` INT NOT NULL AUTO_INCREMENT,
  INDEX `answer_to_question_idx` (`question_id` ASC),
  PRIMARY KEY (`answer_id`),
  CONSTRAINT `answer_to_question`
    FOREIGN KEY (`question_id`)
    REFERENCES `quizsystem`.`Question` (`question_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Result`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Result` (
  `result_id` INT NOT NULL AUTO_INCREMENT,
  `Score` INT NULL,
  `quiz_id` INT NULL,
  `student_answer_id` INT NULL,
  `student_id` INT NULL,
  `date` DATE NULL,
  `duration` INT NULL,
  PRIMARY KEY (`result_id`),
  INDEX `result_to_quiz_idx` (`quiz_id` ASC),
  INDEX `result_to_student_idx` (`student_id` ASC),
  CONSTRAINT `result_to_student`
    FOREIGN KEY (`student_id`)
    REFERENCES `quizsystem`.`Student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `result_to_quiz`
    FOREIGN KEY (`quiz_id`)
    REFERENCES `quizsystem`.`Quiz` (`quiz_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Result_to_Answer`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Result_to_Answer` (
  `result_id` INT NOT NULL,
  `answer_id` INT NOT NULL,
  PRIMARY KEY (`result_id`, `answer_id`),
  INDEX `link_to_answer_idx` (`answer_id` ASC),
  CONSTRAINT `link_to_result`
    FOREIGN KEY (`result_id`)
    REFERENCES `quizsystem`.`Result` (`result_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `link_to_answer`
    FOREIGN KEY (`answer_id`)
    REFERENCES `quizsystem`.`Answer` (`answer_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Module` (
  `module_id` VARCHAR(10) NOT NULL,
  `module_name` VARCHAR(100) NULL,
  PRIMARY KEY (`module_id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Student_to_Module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Student_to_Module` (
  `sutdent_id` INT NOT NULL,
  `module_id` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`sutdent_id`, `module_id`),
  INDEX `link_to_module_idx` (`module_id` ASC),
  CONSTRAINT `link_module_to_student`
    FOREIGN KEY (`sutdent_id`)
    REFERENCES `quizsystem`.`Student` (`student_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `link_student_to_module`
    FOREIGN KEY (`module_id`)
    REFERENCES `quizsystem`.`Module` (`module_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `quizsystem`.`Quiz_to_Module`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `quizsystem`.`Quiz_to_Module` (
  `module_id` VARCHAR(10) NOT NULL,
  `quiz_id` INT NOT NULL,
  PRIMARY KEY (`module_id`, `quiz_id`),
  INDEX `link_to_quiz_idx` (`quiz_id` ASC),
  CONSTRAINT `link_quiz_to_module`
    FOREIGN KEY (`module_id`)
    REFERENCES `quizsystem`.`Module` (`module_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `link_module_to_quiz`
    FOREIGN KEY (`quiz_id`)
    REFERENCES `quizsystem`.`Quiz` (`quiz_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
