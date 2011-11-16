SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';

CREATE SCHEMA IF NOT EXISTS `tourn_201140` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;
USE `tourn_201140`;

-- -----------------------------------------------------
-- Notes about the int "phase" in several tables:
-- 
-- tournament: 0=raw/created, 1=created/viewable, 2=signup open, 3=signup closed, 
--             4=scheduled, 5=active, 6=final
--
-- division: 0=unscheduled, 1=schechuled, 2=active, 3=final
--
-- match: 0=raw/created, 1=scheduled, 2=players assigned(if not first round),
--        3=started, 4=finished
-- 
-- game: 0=created/scheduled(auto created with match), 1=active, 2=final
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Table `tourn_201140`.`location`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`location` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`location` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `address` VARCHAR(45) NOT NULL ,
  `city` VARCHAR(45) NOT NULL ,
  `state` VARCHAR(20) NOT NULL ,
  `zip` VARCHAR(5) NOT NULL ,
  `phone` CHAR(10) NOT NULL ,
  `weekdayOpenTime` TIME NULL ,
  `weekdayCloseTime` TIME NULL ,
  `weekendOpenTime` TIME NULL ,
  `weekendCloseTime` TIME NULL ,
  UNIQUE INDEX (`name`),
  PRIMARY KEY (`id`) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`court`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`court` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`court` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `courtName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`id`) ,
  `id_location`  INT NOT NULL REFERENCES `location`(`id`),
  UNIQUE INDEX (`id_location`, `courtName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`person` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`person` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(45) NOT NULL ,
  `city` VARCHAR(45) NULL DEFAULT NULL ,
  `state` VARCHAR(45) NULL DEFAULT NULL ,
  `phone` CHAR(10) NULL DEFAULT NULL ,
  `gender` CHAR(1) NOT NULL,
  `birthdate` DATE NOT NULL ,
  `unavailTimeStart1` DATETIME NULL DEFAULT NULL ,
  `unavailTimeEnd1` DATETIME NULL DEFAULT NULL ,
  `unavailTimeStart2` DATETIME NULL DEFAULT NULL ,
  `unavailTimeEnd2` DATETIME NULL DEFAULT NULL ,
  UNIQUE INDEX (`email`),
  PRIMARY KEY (`id`) ,
  `id_homeClub`  INT REFERENCES `tourn_201140`.`location`(`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`user` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(45) NOT NULL ,
  `password` VARCHAR(45) NOT NULL ,
  `date_joined` DATE NULL DEFAULT NULL ,
  `permissions` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX (`username`) ,
  `id_person`  INT NOT NULL REFERENCES `person`(`id`),
  UNIQUE INDEX (`id_person`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`tournament`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`tournament` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`tournament` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `start_date` DATE NOT NULL ,
  `end_date` DATE NOT NULL ,
  `isGuestViewable` TINYINT(1)  NOT NULL DEFAULT 1 ,
  `travelTime` INT NOT NULL DEFAULT 0 ,
  `start_time_weekdays` TIME NOT NULL ,
  `end_time_weekdays` TIME NOT NULL ,
  `start_time_weekends` TIME NOT NULL ,
  `end_time_weekends` TIME NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  `maxDivPerPlayer` INT NOT NULL ,
  PRIMARY KEY (`id`) ,
  UNIQUE INDEX (`name`) ,
  `id_owner` INT NOT NULL REFERENCES `user`(`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`venues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`venues` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`venues` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `id_location` INT NOT NULL REFERENCES `location`(`id`),
  `id_tournament` INT NOT NULL REFERENCES `tournament`(`id`),
  UNIQUE INDEX (`id_location`, `id_tournament`),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`division`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`division` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`division` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `isDouble` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `estTime` SMALLINT NOT NULL ,
  `genderConstraint` CHAR(1) DEFAULT NULL,
  `minAge` INT DEFAULT NULL,
  `maxAge` INT DEFAULT NULL,
  `tournType` VARCHAR(45) NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`id`) ,
  `id_tournament`  INT NOT NULL REFERENCES `tournament`(`id`),
  UNIQUE INDEX (`id_tournament`, `name`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`player`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`player` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`player` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`id`),
  `id_player1`  INT NOT NULL REFERENCES `person`(`id`),
  `id_player2`  INT REFERENCES `person`(`id`),
  `id_division`  INT NOT NULL REFERENCES `division`(`id`),
  UNIQUE INDEX(`id_division`, `id_player1`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`match`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`match` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`match` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `startTime` DATETIME NOT NULL ,
  `matchNumber` INT NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  `id_division`  INT NOT NULL REFERENCES `division`(`id`),
  `id_player1`  INT REFERENCES `player`(`id`),
  `id_player2`  INT REFERENCES `player`(`id`),
  `id_court`  INT NOT NULL REFERENCES `court`(`id`),
  UNIQUE INDEX (`matchNumber`, `id_division`),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`game` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`game` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `gameNumber` SMALLINT NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  `team1Score` INT NOT NULL ,
  `team2Score` INT NOT NULL ,
  `id_match`  INT NOT NULL REFERENCES `match`(`id`),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`foul`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`foul` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`foul` (
  `id` INT NOT NULL AUTO_INCREMENT ,
  `foulName` VARCHAR(45) NOT NULL ,
  `penalty` VARCHAR(45) NOT NULL ,
  `foulTime` INT NOT NULL ,
  `id_game` INT NOT NULL REFERENCES `game`(`id`),
  `id_committer` INT NOT NULL REFERENCES `player`(`id`),
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

-- ----------------------------------------------------
-- Insert a bunch of sample data
-- ----------------------------------------------------

INSERT INTO `location` (`name`, `address`, `city`, `state`, `zip`, `phone`, 
	`weekdayOpenTime`, `weekdayCloseTime`, `weekendOpenTime`, `weekendCloseTime`)
VALUES
('Bellingham Athletic Club', '2039 State St', 'Bellingham', 'WA', '98225', '3601234512', CAST('05:30:00' AS TIME), CAST('22:00:00' AS TIME), CAST('07:00:00' AS TIME), CAST('21:00:00' AS TIME)),
('Alderaan Racquetball Club', 'University of Alderaan', 'Aldera', 'Alderaan', '47023', '3601213482', CAST('07:30:00' AS TIME), CAST('18:00:00' AS TIME), CAST('12:00:00' AS TIME), CAST('18:00:00' AS TIME));

SET @bac = (SELECT MAX(`id`) FROM `location` WHERE `name`='Bellingham Athletic Club');
SET @adc = (SELECT MAX(`id`) FROM `location` WHERE `name`='Alderaan Racquetball Club');

INSERT INTO `court` (`courtName`, `id_location`)
VALUES
('A', @bac), ('B', @bac), ('C', @bac), ('D', @bac), ('E', @bac),('A', @adc), ('B', @adc), ('C', @adc);


INSERT INTO `person` (`name`, `email`, `city`, `state`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `id_homeClub`)
VALUES 
('Guest', 'guest@guest.com', null, null, '1111111111', 'g', CAST('1900-1-1' AS DATE), null, null, null, null, null), 
('Luke Skywalker', 'luke@jedi.com', null, null, '3601112222', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME), 
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, @bac),
('Han Solo', 'hansolo@smuggler.com', null, null, '3601234567', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 14:00:00' AS DATETIME),
	CAST('2011-12-03 17:00:00' AS DATETIME), null, null, @bac),
('Leia Organa', 'leia@alderaan.com', null, null, '0123456789', 'f', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('R2D2', 'r2@astromech.com', null, null, '0110001101', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('Chewbacca', 'chewy@kashyyyk.com', null, null, '4250391100', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, @adc),
('Anakin', 'vader@empire.com', null, null, '0621002222', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('Mon Mothma', 'monmothma@rebelalliance.org', null, null, '3601020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Padme Amidala', 'padme@galacticsenate.org', null, null, '3572991212', 'f', CAST('1985-9-12' AS DATE), null, null, null, null, null),
('Obi Wan Kenobe', 'oldben@tatooine.org', null, null, '3657020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('C3-PO', 'threePO@rebelalliance.org', null, null, '3601012256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Jabba the Hutt', 'jabba@tatooine.org', null, null, '3602120256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Yoda', 'yoda@jedi.org', null, null, '3601020126', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Mace Windu', 'windu@jedi.org', null, null, '360560256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Count Dooku', 'tyrannus@cis.org', null, null, '3631070256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Darth Sidious', 'palpatine@deathstar.org', null, null, '3631075344', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Darth Maul', 'maul@sith.com', null, null, '3601020254', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Qui-Gon Jinn', 'quigon@jedi.org', null, null, '3611020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Ki Adi Mundi', 'mundi@jedi.org', null, null, '3601020288', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Uncle Owen', 'owenlars@tatooine.org', null, null, '3602020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Aunt Beru', 'beru@tatooine.org', null, null, '3601049188', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Grand Moff Tarkin', 'tarkin@deathstar.com', null, null, '3601030256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Admiral Ackbar', 'itsatrap@rebelalliance.org', null, null, '3621020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Boba Fett', 'fettsvette@tatooine.org', null, null, '3604020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null,null),
('Jango Fett', 'bountyhunter@kamino.org', null, null, '6601020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Dengar', 'dengar@freelancer.com', null, null, '3601026256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('IG-88', 'ig88@freelancer.com', null, null, '3601020257', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Aayla Secura', 'aayla@jedi.org', null, null, '3601020756', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Luminara Unduli', 'unduli@jedi.org', null, null, '3607020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Shaak Ti', 'shaakti@jedi.org', null, null, '3601020856', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Ahsoka Tano', 'snips@jedi.org', null, null, '3601050256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Asajj Ventress', 'ventress@cis.org', null, null, '3401020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Revan', 'revan@jedi.org', null, null, '3601020258', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Bastila Shan', 'bastila@jedi.org', null, null, '3601020156', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('HK-47', 'killallmeatbags@assassindroids.com', null, null, '3201020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Carth Onasi', 'carthage@endarspire.rep', null, null, '3601320256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Darth Malak', 'malak@sith.org', null, null, '3601020286', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Canderous Ordo', 'mandalore@dxun.org', null, null, '3601620256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Mission Vao', 'mission@taris.org', null, null, '3601120256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Kreia', 'lordofbetrayal@sith.org', null, null, '3603020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Shae Vizla', 'shae@freelancer.org', null, null, '3651020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Jolee Bindo', 'jolee@jedi.org', null, null, '3603020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Visas Marr', 'visas@miraluka.org', null, null, '4601020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Bao-Dur', 'baodur@iridonia.org', null, null, '321020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Atris', 'atris@jedi.org', null, null, '3601020216', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac);
	
INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `id_person`, `id`)
VALUES
('Guest', 'public', CAST(CURRENT_TIMESTAMP AS DATE), null,
    (SELECT `id`
	FROM `person`
	WHERE `name`='Guest'), 0);
	
INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `id_person`)
VALUES
('Chewy', 'AGHDDAHHHHDGHHAGH', CAST(CURRENT_TIMESTAMP AS DATE), null,
	(SELECT `id`
	FROM `person`
	WHERE `name`='Chewbacca')),
('MonMothma', 'p@ssW0rd!?', CAST(CURRENT_TIMESTAMP AS DATE), null, 
	(SELECT `id`
	FROM `person`
	WHERE `name`='Mon Mothma'));
	
INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `id_owner`, `phase`)
VALUES
('WORLD CHAMPIONSHIP 2011', CAST('2011-12-02' AS DATE), CAST('2011-12-04' AS DATE), TRUE, 30, CAST('16:00:00' AS TIME), CAST('21:00:00' AS TIME), CAST('09:00:00' AS TIME), CAST('17:00:00' AS TIME), 3, 
	(SELECT `id` FROM `user` WHERE `username`='MonMothma'), 3),
('Clone Wars Racquetball', CAST('2011-10-13' AS DATE), CAST('2011-10-16' AS DATE), TRUE, 0, CAST('16:00:00' AS TIME), CAST('21:00:00' AS TIME), CAST('09:00:00' AS TIME), CAST('17:00:00' AS TIME), 3, 
	(SELECT `id` FROM `user` WHERE `username`='MonMothma'), 6);
	
SET @tourn = (SELECT MAX(`id`) FROM `tournament` WHERE `name`='WORLD CHAMPIONSHIP 2011');
SET @pasttourn = (SELECT MAX(`id`) FROM `tournament` WHERE `name`='Clone Wars Racquetball');

INSERT INTO `venues` (`id_location`, `id_tournament`)
VALUES
(@bac, @tourn), (@adc, @tourn), (@bac, @pasttourn);


INSERT INTO `division` (`name`, `isDouble`, `estTime`, `genderConstraint`, `minAge`, `maxAge`, `tournType`, `id_tournament`, `phase`)
VALUES
('Singles Men Open', FALSE, 30, 'm', 18, null, 'round robin', @tourn, 0),
('Singles Women Open', FALSE, 30, 'f', 18, null, 'double round robin', @tourn, 0),
('Doubles Co-ed', True, 30, 'a', null, null, 'round robin', @tourn, 0),
('Elite Men', FALSE, 45, 'm', null, null, 'single elimination', @tourn, 0),
('Elite Women', FALSE, 45, 'f', null, null, 'single elimination', @tourn, 0),
('Senior Men', false, 20, 'm', 65, null, 'round robin', @tourn, 0),
('Men Open', FALSE, 30, 'm', null, null, 'single elimination', @pasttourn, 3),
('Women Open', false, 20, 'f', 65, null, 'round robin', @pasttourn, 3),
('Doubles', true, 20, 'a', 65, null, 'round robin', @pasttourn, 3);

SET @smo = (SELECT MAX(`id`) FROM `division` WHERE `name`='Singles Men Open' AND `id_tournament`=@tourn);
SET @dco = (SELECT MAX(`id`) FROM `division` WHERE `name`='Doubles Co-ed' AND `id_tournament`=@tourn);
SET @swo = (SELECT MAX(`id`) FROM `division` WHERE `name`='Singles Women Open' AND `id_tournament`=@tourn);
SET @elm = (SELECT MAX(`id`) FROM `division` WHERE `name`='Elite Men' AND `id_tournament`=@tourn);
SET @elw = (SELECT MAX(`id`) FROM `division` WHERE `name`='Elite Women' AND `id_tournament`=@tourn);
SET @snm = (SELECT MAX(`id`) FROM `division` WHERE `name`='Senior Men' AND `id_tournament`=@tourn);

INSERT INTO `player` (`id_player1`, `id_player2`, `id_division`)
VALUES
((SELECT `id` FROM `person` WHERE `name`='Han Solo'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Uncle Owen'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Chewbacca'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Anakin'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Darth Maul'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Boba Fett'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Jango Fett'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Dengar'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='R2D2'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='C3-PO'), null, @smo),
((SELECT `id` FROM `person` WHERE `name`='Ahsoka Tano'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Atris'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Aunt Beru'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Leia Organa'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Mission Vao'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Mon Mothma'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Padme Amidala'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Shaak Ti'), null, @swo),
((SELECT `id` FROM `person` WHERE `name`='Anakin'), (SELECT `id` FROM `person` WHERE `name`='Ahsoka Tano'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Asajj Ventress'), (SELECT `id` FROM `person` WHERE `name`='Count Dooku'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Jango Fett'), (SELECT `id` FROM `person` WHERE `name`='Boba Fett'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Uncle Owen'), (SELECT `id` FROM `person` WHERE `name`='Aunt Beru'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Obi Wan Kenobe'), (SELECT `id` FROM `person` WHERE `name`='Qui-Gon Jinn'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Han Solo'), (SELECT `id` FROM `person` WHERE `name`='Chewbacca'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Revan'), (SELECT `id` FROM `person` WHERE `name`='Bastila Shan'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Luke Skywalker'), (SELECT `id` FROM `person` WHERE `name`='Leia Organa'), @dco),
((SELECT `id` FROM `person` WHERE `name`='HK-47'), (SELECT `id` FROM `person` WHERE `name`='IG-88'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Carth Onasi'), (SELECT `id` FROM `person` WHERE `name`='Bao-Dur'), @dco),
((SELECT `id` FROM `person` WHERE `name`='Jolee Bindo'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Jabba the Hutt'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Yoda'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Mace Windu'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Qui-Gon Jinn'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Ki Adi Mundi'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Grand Moff Tarkin'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Admiral Ackbar'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='HK-47'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Darth Malak'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Canderous Ordo'), null, @elm),
((SELECT `id` FROM `person` WHERE `name`='Aayla Secura'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Kreia'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Luminara Unduli'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Shae Vizla'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Visas Marr'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Ahsoka Tano'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Leia Organa'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Asajj Ventress'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Bastila Shan'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Atris'), null, @elw),
((SELECT `id` FROM `person` WHERE `name`='Yoda'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Obi Wan Kenobe'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Grand Moff Tarkin'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Qui-Gon Jinn'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Jolee Bindo'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Count Dooku'), null, @snm),
((SELECT `id` FROM `person` WHERE `name`='Chewbacca'), null, @snm);


SET @cwm = (SELECT MAX(`id`) FROM `division` WHERE `name`='Men Open' AND `id_tournament`=@pasttourn);
SET @cww = (SELECT MAX(`id`) FROM `division` WHERE `name`='Women Open' AND `id_tournament`=@pasttourn);
SET @cwd = (SELECT MAX(`id`) FROM `division` WHERE `name`='Doubles' AND `id_tournament`=@pasttourn);

INSERT INTO `player` (`id_player1`, `id_player2`, `id_division`)
VALUES
((SELECT `id` FROM `person` WHERE `name`='Anakin'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='R2D2'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Obi Wan Kenobe'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Mace Windu'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Boba Fett'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Count Dooku'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Darth Maul'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Darth Sidious'), null, @cwm),
((SELECT `id` FROM `person` WHERE `name`='Shaak Ti'), null, @cww),
((SELECT `id` FROM `person` WHERE `name`='Ahsoka Tano'), null, @cww),
((SELECT `id` FROM `person` WHERE `name`='Aayla Secura'), null, @cww),
((SELECT `id` FROM `person` WHERE `name`='Luminara Unduli'), null, @cww),
((SELECT `id` FROM `person` WHERE `name`='Asajj Ventress'), null, @cww),
((SELECT `id` FROM `person` WHERE `name`='Anakin'), (SELECT `id` FROM `person` WHERE `name`='Obi Wan Kenobe'), @cwd),
((SELECT `id` FROM `person` WHERE `name`='R2D2'), (SELECT `id` FROM `person` WHERE `name`='Chewbacca'), @cwd),
((SELECT `id` FROM `person` WHERE `name`='Asajj Ventress'), (SELECT `id` FROM `person` WHERE `name`='Count Dooku'), @cwd),
((SELECT `id` FROM `person` WHERE `name`='Darth Maul'), (SELECT `id` FROM `person` WHERE `name`='Darth Sidious'), @cwd);

SET @courta = (SELECT MAX(`id`) FROM `court` WHERE `courtName`='A' AND `id_location`=@adc);
SET @courtb = (SELECT MAX(`id`) FROM `court` WHERE `courtName`='B' AND `id_location`=@adc);
SET @courtc = (SELECT MAX(`id`) FROM `court` WHERE `courtName`='C' AND `id_location`=@adc);

SET @playerAnakin = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Anakin') AND `id_division`= @cwm);
SET @playerR2 = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='R2D2') AND `id_division`= @cwm);
SET @playerObi = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Obi Wan Kenobe') AND `id_division`= @cwm);
SET @playerWindu = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Mace Windu') AND `id_division`= @cwm);
SET @playerFett = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Boba Fett') AND `id_division`= @cwm);
SET @playerDooku = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Count Dooku') AND `id_division`= @cwm);
SET @playerMaul = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Darth Maul') AND `id_division`= @cwm);
SET @playerSidious = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Darth Sidious') AND `id_division`= @cwm);

SET @playerAhsoka = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Ahsoka Tano') AND `id_division`= @cww);
SET @playerShaak = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Shaak Ti') AND `id_division`= @cww);
SET @playerSecura = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Aayla Secura') AND `id_division`= @cww);
SET @playerUnduli = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Luminara Unduli') AND `id_division`= @cww);
SET @playerVentress = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Asajj Ventress') AND `id_division`= @cww);

SET @doubleAnakin = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Anakin') AND `id_division`= @cwd);
SET @doubleR2D2 = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='R2D2') AND `id_division`= @cwd);
SET @doubleVentress = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Asajj Ventress') AND `id_division`= @cwd);
SET @doubleMaul = (SELECT MAX(`id`) FROM `player` WHERE `id_player1`=(SELECT `id` FROM `person` WHERE `name`='Darth Maul') AND `id_division`= @cwd);

INSERT INTO `match` (`startTime`, `matchNumber`, `phase`, `id_division`, `id_player1`, `id_player2`, `id_court`)
VALUES
(CAST('2011-10-13T16:00:00' AS DATETIME), 1, 4, @cwd, @doubleAnakin, @doubleR2D2, @courta),
(CAST('2011-10-13T16:30:00' AS DATETIME), 2, 4, @cwd, @doubleAnakin, @doubleMaul, @courtb),
(CAST('2011-10-13T17:00:00' AS DATETIME), 3, 4, @cwd, @doubleAnakin, @doubleVentress, @courtc),
(CAST('2011-10-13T17:30:00' AS DATETIME), 4, 4, @cwd, @doubleR2D2, @doubleMaul, @courta),
(CAST('2011-10-13T18:00:00' AS DATETIME), 5, 4, @cwd, @doubleR2D2, @doubleVentress, @courtb),
(CAST('2011-10-13T18:00:00' AS DATETIME), 6, 4, @cwd, @doubleMaul, @doubleVentress, @courtc),
(CAST('2011-10-14T16:00:00' AS DATETIME), 1, 4, @cww, @playerAhsoka, @playerShaak, @courta),
(CAST('2011-10-14T16:03:00' AS DATETIME), 2, 4, @cww, @playerAhsoka, @playerSecura, @courtb),
(CAST('2011-10-14T17:00:00' AS DATETIME), 3, 4, @cww, @playerAhsoka, @playerUnduli, @courtc),
(CAST('2011-10-14T17:30:00' AS DATETIME), 4, 4, @cww, @playerAhsoka, @playerVentress, @courta),
(CAST('2011-10-14T18:30:00' AS DATETIME), 5, 4, @cww, @playerShaak, @playerSecura, @courtb),
(CAST('2011-10-14T18:00:00' AS DATETIME), 6, 4, @cww, @playerShaak, @playerUnduli, @courta),
(CAST('2011-10-14T19:30:00' AS DATETIME), 7, 4, @cww, @playerShaak, @playerVentress, @courta),
(CAST('2011-10-14T19:00:00' AS DATETIME), 8, 4, @cww, @playerSecura, @playerUnduli, @courta),
(CAST('2011-10-14T20:00:00' AS DATETIME), 9, 4, @cww, @playerSecura, @playerVentress, @courta),
(CAST('2011-10-14T20:30:00' AS DATETIME), 10, 4, @cww, @playerUnduli, @playerVentress, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 1, 4, @cwm, @playerAnakin, @playerR2, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 2, 4, @cwm, @playerObi, @playerWindu, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 3, 4, @cwm, @playerFett, @playerDooku, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 4, 4, @cwm, @playerMaul, @playerSidious, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 5, 4, @cwm, @playerAnakin, @playerWindu, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 6, 4, @cwm, @playerDooku, @playerSidious, @courta),
(CAST('2011-10-15T09:00:00' AS DATETIME), 7, 4, @cwm, @playerSidious, @playerAnakin, @courta);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
