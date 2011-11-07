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
  `lid` INT NOT NULL AUTO_INCREMENT ,
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
  PRIMARY KEY (`lid`) )
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`court`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`court` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`court` (
  `cid` INT NOT NULL AUTO_INCREMENT ,
  `courtName` VARCHAR(45) NOT NULL ,
  PRIMARY KEY (`cid`) ,
  `lid_location`  INT NOT NULL REFERENCES `location`(`lid`),
  UNIQUE INDEX (`lid_location`, `courtName`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`person`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`person` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`person` (
  `pid` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL ,
  `email` VARCHAR(45) NOT NULL ,
  `address` VARCHAR(70) NULL DEFAULT NULL ,
  `phone` CHAR(10) NULL DEFAULT NULL ,
  `gender` CHAR(1) NOT NULL,
  `birthdate` DATE NOT NULL ,
  `unavailTimeStart1` DATETIME NULL DEFAULT NULL ,
  `unavailTimeEnd1` DATETIME NULL DEFAULT NULL ,
  `unavailTimeStart2` DATETIME NULL DEFAULT NULL ,
  `unavailTimeEnd2` DATETIME NULL DEFAULT NULL ,
  UNIQUE INDEX (`email`),
  PRIMARY KEY (`pid`) ,
  `lid_homeClub`  INT REFERENCES `tourn_201140`.`location`(`lid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`user`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`user` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`user` (
  `uid` INT NOT NULL AUTO_INCREMENT ,
  `username` VARCHAR(45) NOT NULL ,
  `password` VARCHAR(45) NOT NULL ,
  `date_joined` DATE NULL DEFAULT NULL ,
  `permissions` VARCHAR(45) NULL DEFAULT NULL ,
  PRIMARY KEY (`uid`) ,
  UNIQUE INDEX (`username`) ,
  `pid_person`  INT NOT NULL REFERENCES `person`(`pid`),
  UNIQUE INDEX (`pid_person`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`tournament`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`tournament` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`tournament` (
  `tid` INT NOT NULL AUTO_INCREMENT ,
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
  PRIMARY KEY (`tid`) ,
  UNIQUE INDEX (`name`) ,
  `uid_owner` INT NOT NULL REFERENCES `user`(`uid`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`venues`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`venues` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`venues` (
  `vid` INT NOT NULL AUTO_INCREMENT ,
  `lid_location` INT NOT NULL REFERENCES `location`(`lid`),
  `tid_tournament` INT NOT NULL REFERENCES `tournament`(`tid`),
  UNIQUE INDEX (`lid_location`, `tid_tournament`),
  PRIMARY KEY (`vid`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`division`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`division` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`division` (
  `did` INT NOT NULL AUTO_INCREMENT ,
  `name` VARCHAR(45) NOT NULL ,
  `isDouble` TINYINT(1)  NOT NULL DEFAULT 0 ,
  `estTime` SMALLINT NOT NULL ,
  `genderConstraint` CHAR(1) DEFAULT NULL,
  `minAge` INT DEFAULT NULL,
  `maxAge` INT DEFAULT NULL,
  `tournType` VARCHAR(45) NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  PRIMARY KEY (`did`) ,
  `tid_tournament`  INT NOT NULL REFERENCES `tournament`(`tid`),
  UNIQUE INDEX (`tid_tournament`, `name`))
ENGINE = InnoDB;

-- -----------------------------------------------------
-- Table `tourn_201140`.`player`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`player` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`player` (
  `plid` INT NOT NULL AUTO_INCREMENT ,
  PRIMARY KEY (`plid`),
  `pid_player1`  INT NOT NULL REFERENCES `person`(`pid`),
  `pid_player2`  INT REFERENCES `person`(`pid`),
  `did_division`  INT NOT NULL REFERENCES `division`(`did`),
  UNIQUE INDEX(`did_division`, `pid_player1`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`match`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`match` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`match` (
  `mid` INT NOT NULL AUTO_INCREMENT ,
  `startTime` DATETIME NOT NULL ,
  `endTime` DATETIME NOT NULL ,
  `matchNumber` INT NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  `did_division`  INT NOT NULL REFERENCES `division`(`did`),
  `plid_player1`  INT REFERENCES `player`(`plid`),
  `plid_player2`  INT REFERENCES `player`(`plid`),
  `cid_court`  INT NOT NULL REFERENCES `court`(`cid`),
  UNIQUE INDEX (`matchNumber`, `did_division`),
  PRIMARY KEY (`mid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`game`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`game` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`game` (
  `gid` INT NOT NULL AUTO_INCREMENT ,
  `gameNumber` SMALLINT NOT NULL ,
  `phase` SMALLINT NOT NULL DEFAULT 0 ,
  `team1Score` INT NOT NULL ,
  `team2Score` INT NOT NULL ,
  `mid_match`  INT NOT NULL REFERENCES `match`(`mid`),
  PRIMARY KEY (`gid`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `tourn_201140`.`foul`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `tourn_201140`.`foul` ;

CREATE  TABLE IF NOT EXISTS `tourn_201140`.`foul` (
  `fid` INT NOT NULL AUTO_INCREMENT ,
  `foulName` VARCHAR(45) NOT NULL ,
  `penalty` VARCHAR(45) NOT NULL ,
  `foulTime` TIME NOT NULL ,
  `gid_game` INT NOT NULL REFERENCES `game`(`gid`),
  `plid_committer` INT NOT NULL REFERENCES `player`(`plid`),
  PRIMARY KEY (`fid`))
ENGINE = InnoDB;


INSERT INTO `location` (`name`, `address`, `city`, `state`, `zip`, `phone`, 
	`weekdayOpenTime`, `weekdayCloseTime`, `weekendOpenTime`, `weekendCloseTime`)
VALUES
('Bellingham Athletic Club', '2039 State St', 'Bellingham', 'WA', '98225', '3601234512', CAST('05:30:00' AS TIME), CAST('22:00:00' AS TIME), CAST('07:00:00' AS TIME), CAST('21:00:00' AS TIME)),
('Alderaan Racquetball Club', 'University of Alderaan', 'Aldera', 'Alderaan', '47023', '3601213482', CAST('07:30:00' AS TIME), CAST('18:00:00' AS TIME), CAST('12:00:00' AS TIME), CAST('18:00:00' AS TIME));

SET @bac = (SELECT MAX(`lid`) FROM `location` WHERE `name`='Bellingham Athletic Club');
SET @adc = (SELECT MAX(`lid`) FROM `location` WHERE `name`='Alderaan Racquetball Club');

INSERT INTO `court` (`courtName`, `lid_location`)
VALUES
('A', @bac), ('B', @bac), ('C', @bac), ('D', @bac), ('E', @bac),('A', @adc), ('B', @adc), ('C', @adc);


INSERT INTO `person` (`name`, `email`, `address`, `phone`, `gender`, `birthdate`, `unavailTimeStart1`, `unavailTimeEnd1`, `unavailTimeStart2`, `unavailTimeEnd2`, `lid_homeClub`)
VALUES 
('Guest', 'guest@guest.com', null, '1111111111', 'g', CAST('1900-1-1' AS DATE), null, null, null, null, null), 
('Luke Skywalker', 'luke@jedi.com', null, '3601112222', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME), 
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, @bac),
('Han Solo', 'hansolo@smuggler.com', null, '3601234567', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 14:00:00' AS DATETIME),
	CAST('2011-12-03 17:00:00' AS DATETIME), null, null, @bac),
('Leia Organa', 'leia@alderaan.com', null, '0123456789', 'f', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('R2D2', 'r2@astromech.com', null, '0110001101', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('Chewbacca', 'chewy@kashyyyk.com', null, '4250391100', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, @adc),
('Anakin', 'vader@empire.com', null, '0621002222', 'm', CAST('1980-03-20' AS DATE), CAST('2011-12-03 12:00:00' AS DATETIME),
	CAST('2011-12-03 13:00:00' AS DATETIME), null, null, null),
('Mon Mothma', 'monmothma@rebelalliance.org', null, '3601020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Padme Amidala', 'padme@galacticsenate.org', null, '3572991212', 'f', CAST('1985-9-12' AS DATE), null, null, null, null, null),
('Obi Wan Kenobe', 'oldben@tatooine.org', null, '3657020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('C3-PO', 'threePO@rebelalliance.org', null, '3601012256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Jabba the Hutt', 'jabba@tatooine.org', null, '3602120256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Yoda', 'yoda@jedi.org', null, '3601020126', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Mace Windu', 'windu@jedi.org', null, '360560256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Count Dooku', 'tyrannus@cis.org', null, '3631070256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Darth Maul', 'maul@sith.com', null, '3601020254', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Qui-Gon Jinn', 'quigon@jedi.org', null, '3611020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Ki Adi Mundi', 'mundi@jedi.org', null, '3601020288', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Uncle Owen', 'owenlars@tatooine.org', null, '3602020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Aunt Beru', 'beru@tatooine.org', null, '3601049188', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Grand Moff Tarkin', 'tarkin@deathstar.com', null, '3601030256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Admiral Ackbar', 'itsatrap@rebelalliance.org', null, '3621020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Boba Fett', 'fettsvette@tatooine.org', null, '3604020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null,null),
('Jango Fett', 'bountyhunter@kamino.org', null, '6601020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Dengar', 'dengar@freelancer.com', null, '3601026256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('IG-88', 'ig88@freelancer.com', null, '3601020257', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Aayla Secura', 'aayla@jedi.org', null, '3601020756', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Luminara Unduli', 'unduli@jedi.org', null, '3607020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Shaak Ti', 'shaakti@jedi.org', null, '3601020856', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Ahsoka Tano', 'snips@jedi.org', null, '3601050256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Asajj Ventress', 'ventress@cis.org', null, '3401020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @adc),
('Revan', 'revan@jedi.org', null, '3601020258', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Bastila Shan', 'bastila@jedi.org', null, '3601020156', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('HK-47', 'killallmeatbags@assassindroids.com', null, '3201020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Carth Onasi', 'carthage@endarspire.rep', null, '3601320256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, @bac),
('Darth Malak', 'malak@sith.org', null, '3601020286', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Canderous Ordo', 'mandalore@dxun.org', null, '3601620256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Mission Vao', 'mission@taris.org', null, '3601120256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Kreia', 'lordofbetrayal@sith.org', null, '3603020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Shae Vizla', 'shae@freelancer.org', null, '3651020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Jolee Bindo', 'jolee@jedi.org', null, '3603020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Visas Marr', 'visas@miraluka.org', null, '4601020256', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Bao-Dur', 'baodur@iridonia.org', null, '321020256', 'm', CAST('1975-9-12' AS DATE), null, null, null, null, null),
('Atris', 'atris@jedi.org', null, '3601020216', 'f', CAST('1975-9-12' AS DATE), null, null, null, null, @bac);
	
INSERT INTO `user` (`username`, `password`, `date_joined`, `permissions`, `pid_person`)
VALUES
('Guest', 'k6?0dQ!SqU&RlD(8k', CAST(CURRENT_TIMESTAMP AS DATE), null,
    (SELECT `pid`
	FROM `person`
	WHERE `name`='Guest')),
('Chewy', 'AGHDDAHHHHDGHHAGH', CAST(CURRENT_TIMESTAMP AS DATE), null,
	(SELECT `pid`
	FROM `person`
	WHERE `name`='Chewbacca')),
('MonMothma', 'p@ssW0rd!?', CAST(CURRENT_TIMESTAMP AS DATE), null, 
	(SELECT `pid`
	FROM `person`
	WHERE `name`='Mon Mothma'));
	
INSERT INTO `tournament` (`name`, `start_date`, `end_date`, `isGuestViewable`, `travelTime`, `start_time_weekdays`, `end_time_weekdays`, `start_time_weekends`, `end_time_weekends`, `maxDivPerPlayer`, `uid_owner`)
VALUES
('WORLD CHAMPIONSHIP 2011', CAST('2011-12-02' AS DATE), CAST('2011-12-04' AS DATE), TRUE, 30, CAST('16:00:00' AS TIME), CAST('21:00:00' AS TIME), CAST('09:00:00' AS TIME), CAST('17:00:00' AS TIME), 3, 
	(SELECT `uid` FROM `user` WHERE `username`='MonMothma')),
('Clone Wars Racquetball', CAST('2011-10-13' AS DATE), CAST('2011-10-16' AS DATE), TRUE, 0, CAST('16:00:00' AS TIME), CAST('21:00:00' AS TIME), CAST('09:00:00' AS TIME), CAST('17:00:00' AS TIME), 3, 
	(SELECT `uid` FROM `user` WHERE `username`='MonMothma'));
	
SET @tourn = (SELECT MAX(`tid`) FROM `tournament` WHERE `name`='WORLD CHAMPIONSHIP 2011');
SET @pasttourn = (SELECT MAX (`tid`) FROM `tournament` WHERE `name`='Clone Wars Racquetball');

INSERT INTO `venues` (`lid_location`, `tid_tournament`)
VALUES
(@bac, @tourn), (@adc, @tourn), (@bac, pasttourn);


INSERT INTO `division` (`name`, `isDouble`, `estTime`, `genderConstraint`, `minAge`, `maxAge`, `tournType`, `tid_tournament`)
VALUES
('Singles Men Open', FALSE, 30, 'm', 18, null, 'round robin', @tourn),
('Singles Women Open', FALSE, 30, 'f', 18, null, 'double round robin', @tourn),
('Doubles Co-ed', True, 30, 'a', null, null, 'round robin', @tourn),
('Elite Men', FALSE, 45, 'm', null, null, 'single elimination', @tourn),
('Elite Women', FALSE, 45, 'f', null, null, 'single elimination', @tourn),
('Senior Men', false, 20, 'm', 65, null, 'round robin', @tourn);

SET @smo = (SELECT MAX(`did`) FROM `division` WHERE `name`='Singles Men Open' AND `tid_tournament`=@tourn);
SET @dco = (SELECT MAX(`did`) FROM `division` WHERE `name`='Doubles Co-ed' AND `tid_tournament`=@tourn);
SET @swo = (SELECT MAX(`did`) FROM `division` WHERE `name`='Singles Women Open' AND `tid_tournament`=@tourn);
SET @elm = (SELECT MAX(`did`) FROM `division` WHERE `name`='Elite Men' AND `tid_tournament`=@tourn);
SET @elw = (SELECT MAX(`did`) FROM `division` WHERE `name`='Elite Women' AND `tid_tournament`=@tourn);
SET @snm = (SELECT MAX(`did`) FROM `division` WHERE `name`='Senior Men' AND `tid_tournament`=@tourn);

INSERT INTO `player` (`pid_player1`, `pid_player2`, `did_division`)
VALUES
((SELECT `pid` FROM `person` WHERE `name`='Han Solo'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Uncle Owen'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Chewbacca'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Anakin'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Darth Maul'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Boba Fett'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Jango Fett'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Dengar'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='R2D2'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='C3-PO'), null, @smo),
((SELECT `pid` FROM `person` WHERE `name`='Ahsoka Tano'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Atris'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Aunt Beru'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Leia Organa'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Mission Vao'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Mon Mothma'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Padme Amidala'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Shaak Ti'), null, @swo),
((SELECT `pid` FROM `person` WHERE `name`='Anakin'), (SELECT `pid` FROM `person` WHERE `name`='Ahsoka Tano'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Asajj Ventress'), (SELECT `pid` FROM `person` WHERE `name`='Count Dooku'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Jango Fett'), (SELECT `pid` FROM `person` WHERE `name`='Boba Fett'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Uncle Owen'), (SELECT `pid` FROM `person` WHERE `name`='Aunt Beru'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Obi Wan Kenobe'), (SELECT `pid` FROM `person` WHERE `name`='Qui-Gon Jinn'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Han Solo'), (SELECT `pid` FROM `person` WHERE `name`='Chewbacca'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Revan'), (SELECT `pid` FROM `person` WHERE `name`='Bastila Shan'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Luke Skywalker'), (SELECT `pid` FROM `person` WHERE `name`='Leia Organa'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='HK-47'), (SELECT `pid` FROM `person` WHERE `name`='IG-88'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Carth Onasi'), (SELECT `pid` FROM `person` WHERE `name`='Bao-Dur'), @dco),
((SELECT `pid` FROM `person` WHERE `name`='Jolee Bindo'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Jabba the Hutt'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Yoda'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Mace Windu'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Qui-Gon Jinn'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Ki Adi Mundi'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Grand Moff Tarkin'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Admiral Ackbar'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='HK-47'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Darth Malak'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Canderous Ordo'), null, @elm),
((SELECT `pid` FROM `person` WHERE `name`='Aayla Secura'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Kreia'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Luminara Unduli'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Shae Vizla'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Visas Marr'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Ahsoka Tano'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Leia Organa'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Asajj Ventress'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Bastila Shan'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Atris'), null, @elw),
((SELECT `pid` FROM `person` WHERE `name`='Yoda'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Obi Wan Kenobe'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Grand Moff Tarkin'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Qui-Gon Jinn'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Jolee Bindo'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Count Dooku'), null, @snm),
((SELECT `pid` FROM `person` WHERE `name`='Chewbacca'), null, @snm);

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
