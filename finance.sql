-- phpMyAdmin SQL Dump
-- version 3.3.9
-- http://www.phpmyadmin.net
--
-- Host: localhost
-- Generation Time: Mar 02, 2014 at 08:13 PM
-- Server version: 5.1.53
-- PHP Version: 5.3.4

SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `yahoofinance`
--
CREATE DATABASE `yahoofinance` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `yahoofinance`;

-- --------------------------------------------------------

--
-- Table structure for table `stockdailyupdates`
--

CREATE TABLE IF NOT EXISTS `stockdailyupdates` (
  `symbol` varchar(250) NOT NULL DEFAULT '',
  `dayHigh` varchar(250) DEFAULT NULL,
  `dayLow` varchar(250) DEFAULT NULL,
  `ePS` varchar(250) DEFAULT NULL,
  `marketcap` varchar(250) DEFAULT NULL,
  `Movingav50day` varchar(250) DEFAULT NULL,
  `pe` varchar(250) DEFAULT NULL,
  `price` varchar(250) DEFAULT NULL,
  `volume` varchar(250) DEFAULT NULL,
  `week52high` varchar(250) DEFAULT NULL,
  `week52low` varchar(250) DEFAULT NULL,
  PRIMARY KEY (`symbol`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;

--
-- Dumping data for table `stockdailyupdates`
--

