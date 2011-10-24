#!/usr/bin/python
# animal.py - create animal table and
# retrieve information from it

import sys
import MySQLdb

try:
        print "Connecting to database"
        db = MySQLdb.connect("srproj.cs.wwu.edu","admtourn201140","yinvamOph","tourn_201140" )
        cursor = db.cursor()

        cursor.execute("SET max_error_count=0;")
        
        cursor.execute("SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;")
        cursor.execute("SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;")
        cursor.execute("SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL';")

        cursor.execute("CREATE SCHEMA IF NOT EXISTS `tourn_201140` \
        DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci ;")
        
        cursor.execute("USE `tourn_201140`;")
        
        cursor.execute("DROP TABLE IF EXISTS `location`;")
        cursor.execute("DROP TABLE IF EXISTS `person`;")
        cursor.execute("DROP TABLE IF EXISTS `user`;")
        cursor.execute("DROP TABLE IF EXISTS `tournament`;")
        cursor.execute("DROP TABLE IF EXISTS `division`;")
        cursor.execute("DROP TABLE IF EXISTS `court`;")
        cursor.execute("DROP TABLE IF EXISTS `player`;")
        cursor.execute("DROP TABLE IF EXISTS `match`;")
        cursor.execute("DROP TABLE IF EXISTS `game`;")
        cursor.execute("DROP TABLE IF EXISTS `foul`;")
        cursor.execute("DROP TABLE IF EXISTS `venue`;")
        
        cursor.execute("CREATE TABLE location \
        (`lid` INT NOT NULL AUTO_INCREMENT , \
        `name` VARCHAR(45) NOT NULL , \
        `address` VARCHAR(45) NOT NULL , `city` VARCHAR(45) NOT NULL , \
        `state` VARCHAR(20) NOT NULL , `zip` VARCHAR(5) NOT NULL , \
        `phone` VARCHAR(10) NOT NULL , `weekdayOpenTime` TIME NULL , \
        `weekdayCloseTime` TIME NULL , `weekendOpenTime` TIME NULL , \
        `weekendCloseTime` TIME NULL ,  PRIMARY KEY (`lid`) , UNIQUE INDEX \
        `vid_UNIQUE` (`lid` ASC) );")

        cursor.execute("CREATE TABLE `person` \
        (`pid` INT NOT NULL AUTO_INCREMENT , \
        `name` VARCHAR(45) NOT NULL , \
        `email` VARCHAR(45) NOT NULL , \
        `address` VARCHAR(45) NULL DEFAULT NULL , \
        `phone` VARCHAR(10) NULL DEFAULT NULL , \
        `birthdate` DATE NOT NULL , \
        `unavailTimeStart1` DATETIME NULL DEFAULT NULL , \
        `unavailTimeStart2` DATETIME NULL DEFAULT NULL , \
        `unavailTimeEnd1` DATETIME NULL DEFAULT NULL , \
        `unavailTimeEnd2` DATETIME NULL DEFAULT NULL , \
        `lid_homeClub` INT NULL , \
        `uid_user` INT NULL , \
        PRIMARY KEY (`pid`) ,\
        UNIQUE INDEX `pid_UNIQUE` (`pid` ASC) , \
        UNIQUE INDEX `Email_UNIQUE` (`email` ASC) , \
        INDEX `uid` (`uid_user` ASC) , \
        INDEX `vid` (`lid_homeClub` ASC) , \
        CONSTRAINT `uid` \
        FOREIGN KEY (`uid_user` ) \
        REFERENCES `tourn_201140`.`user` (`uid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `vid` \
        FOREIGN KEY (`lid_homeClub` ) \
        REFERENCES `tourn_201140`.`location` (`lid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE TABLE `user` \
        (`uid` INT NOT NULL AUTO_INCREMENT , \
        `username` VARCHAR(45) NOT NULL , \
        `password` VARCHAR(45) NOT NULL , \
        `date_joined` DATE NULL DEFAULT NULL , \
        `permissions` VARCHAR(45) NULL DEFAULT NULL , \
        `pid_person` INT NULL , \
        PRIMARY KEY (`uid`) , \
        UNIQUE INDEX `uid_UNIQUE` (`uid` ASC) , \
        UNIQUE INDEX `username_UNIQUE` (`username` ASC) , \
        INDEX `pid` (`pid_person` ASC) , \
        CONSTRAINT `pid` \
        FOREIGN KEY (`pid_person` ) \
        REFERENCES `tourn_201140`.`person` (`pid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `tournament` \
        (`tid` INT NOT NULL AUTO_INCREMENT , \
        `name` VARCHAR(45) NOT NULL , \
        `start_date` DATE NOT NULL , \
        `end_date` DATE NOT NULL , \
        `isGuestViewable` TINYINT(1)  NOT NULL DEFAULT 1 , \
        `travelTime` INT NOT NULL DEFAULT 0 , \
        `start_time_weekdays` TIME NOT NULL , \
        `end_time_weekdays` TIME NOT NULL , \
        `start_time_weekends` TIME NOT NULL , \
        `end_time_weekends` TIME NOT NULL , \
        `phase` INT NOT NULL DEFAULT 0 , \
        `maxDivPerPlayer` INT NOT NULL , \
        `uid_owner` INT NOT NULL , \
        PRIMARY KEY (`tid`) , \
        UNIQUE INDEX `tid_UNIQUE` (`tid` ASC) , \
        UNIQUE INDEX `Name_UNIQUE` (`name` ASC) , \
        INDEX `uid` (`uid_owner` ASC) , \
        CONSTRAINT `uid` \
        FOREIGN KEY (`uid_owner` ) \
        REFERENCES `tourn_201140`.`user` (`uid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `division` ( \
        `did` INT NOT NULL AUTO_INCREMENT , \
        `isDouble` TINYINT(1)  NOT NULL DEFAULT 0 , \
        `name` VARCHAR(45) NOT NULL , \
        `estTime` INT NOT NULL , \
        `tournType` VARCHAR(45) NOT NULL , \
        `tid_tournament` INT NOT NULL , \
        PRIMARY KEY (`did`) , \
        UNIQUE INDEX `did_UNIQUE` (`did` ASC) , \
        INDEX `tid` (`tid_tournament` ASC) , \
        CONSTRAINT `tid` \
        FOREIGN KEY (`tid_tournament` ) \
        REFERENCES `tourn_201140`.`tournament` (`tid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `court` ( \
        `cid` INT NOT NULL AUTO_INCREMENT , \
        `courtName` VARCHAR(45) NOT NULL , \
        `lid_location` INT NOT NULL , \
        PRIMARY KEY (`cid`) , \
        UNIQUE INDEX `cid_UNIQUE` (`cid` ASC) , \
        INDEX `lid` (`lid_location` ASC) , \
        CONSTRAINT `lid` \
        FOREIGN KEY (`lid_location` ) \
        REFERENCES `tourn_201140`.`location` (`lid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `player` ( \
        `plid` INT NOT NULL AUTO_INCREMENT , \
        `pid_player1` INT NOT NULL , \
        `pid_player2` INT DEFAULT NULL , \
        `did` INT NOT NULL , \
        INDEX `pid_player1` (`pid_player1` ASC) , \
        INDEX `did` (`did` ASC) , \
        PRIMARY KEY (`plid`) , \
        INDEX `pid_player2` (`pid_player2` ASC) , \
        UNIQUE INDEX `plid_UNIQUE` (`plid` ASC) , \
        CONSTRAINT `pid_player1` \
        FOREIGN KEY (`pid_player1` ) \
        REFERENCES `tourn_201140`.`person` (`pid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `did` \
        FOREIGN KEY (`did` ) \
        REFERENCES `tourn_201140`.`division` (`did` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `pid_player2` \
        FOREIGN KEY (`pid_player2` ) \
        REFERENCES `tourn_201140`.`person` (`pid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `match` ( \
        `mid` INT NOT NULL AUTO_INCREMENT , \
        `startTime` DATETIME NOT NULL , \
        `endTime` DATETIME NOT NULL , \
        `matchNumber` INT NOT NULL , \
        `phase` INT NOT NULL DEFAULT 0 , \
        `did_division` INT NOT NULL , \
        `plid_player1` INT NOT NULL , \
        `plid_player2` INT NOT NULL , \
        `cid_court` INT NOT NULL , \
        PRIMARY KEY (`mid`) , \
        UNIQUE INDEX `mid_UNIQUE` (`mid` ASC) , \
        INDEX `did` (`did_division` ASC) , \
        INDEX `plid_player1` (`plid_player1` ASC) , \
        INDEX `plid_player2` (`plid_player2` ASC) , \
        INDEX `cid` (`cid_court` ASC) , \
        CONSTRAINT `did` \
        FOREIGN KEY (`did_division` ) \
        REFERENCES `tourn_201140`.`division` (`did` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `plid` \
        FOREIGN KEY (`plid_player1` ) \
        REFERENCES `tourn_201140`.`player` (`plid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `plid` \
        FOREIGN KEY (`plid_player2` ) \
        REFERENCES `tourn_201140`.`player` (`plid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `cid` \
        FOREIGN KEY (`cid_court` ) \
        REFERENCES `tourn_201140`.`court` (`cid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `game` ( \
        `gid` INT NOT NULL , \
        `gameNo` INT NOT NULL , \
        `phase` INT NOT NULL DEFAULT 0 , \
        `team1Score` INT NOT NULL , \
        `team2Score` INT NOT NULL , \
        `mid_match` INT NOT NULL , \
        PRIMARY KEY (`gid`) , \
        UNIQUE INDEX `gid_UNIQUE` (`gid` ASC) , \
        INDEX `mid` (`mid_match` ASC) , \
        CONSTRAINT `mid` \
        FOREIGN KEY (`mid_match` ) \
        REFERENCES `tourn_201140`.`match` (`mid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `foul` ( \
        `fid` INT NOT NULL AUTO_INCREMENT , \
        `foulName` VARCHAR(45) NOT NULL , \
        `penalty` VARCHAR(45) NOT NULL , \
        `foulTime` TIME NOT NULL , \
        `gid_game` INT NOT NULL , \
        `plid_committer` INT NOT NULL , \
        PRIMARY KEY (`fid`) , \
        UNIQUE INDEX `fid_UNIQUE` (`fid` ASC) , \
        INDEX `gid` (`gid_game` ASC) , \
        INDEX `plid` (`plid_committer` ASC) , \
        CONSTRAINT `gid` \
        FOREIGN KEY (`gid_game` ) \
        REFERENCES `tourn_201140`.`game` (`gid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `plid` \
        FOREIGN KEY (`plid_committer` ) \
        REFERENCES `tourn_201140`.`player` (`plid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")

        cursor.execute("CREATE  TABLE  `venue` ( \
        `lid_location` INT NOT NULL , \
        `tid_tournament` INT NOT NULL , \
        PRIMARY KEY (`lid_location`, `tid_tournament`) , \
        INDEX `lid` (`lid_location` ASC) , \
        INDEX `tid` (`tid_tournament` ASC) , \
        CONSTRAINT `lid` \
        FOREIGN KEY (`lid_location` ) \
        REFERENCES `tourn_201140`.`location` (`lid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION, \
        CONSTRAINT `tid` \
        FOREIGN KEY (`tid_tournament` ) \
        REFERENCES `tourn_201140`.`tournament` (`tid` ) \
        ON DELETE NO ACTION \
        ON UPDATE NO ACTION);")
        
        cursor.execute("SET SQL_MODE=@OLD_SQL_MODE;")
        cursor.execute("SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;")
        cursor.execute("SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;")

        print "Closing connection to database"
        db.close()
        
except MySQLdb.Error, e:
        print "Error %d: %s" % (e.args[0], e.args[1])
        print "Aborting."
        sys.exit (1)
