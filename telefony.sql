-- phpMyAdmin SQL Dump
-- version 4.4.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Czas generowania: 23 Gru 2015, 17:13
-- Wersja serwera: 5.6.26
-- Wersja PHP: 5.6.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Baza danych: `telefony`
--

-- --------------------------------------------------------

--
-- Struktura tabeli dla tabeli `contacts`
--

CREATE TABLE IF NOT EXISTS `contacts` (
  `name` varchar(64) COLLATE utf8_polish_ci NOT NULL,
  `surname` varchar(64) COLLATE utf8_polish_ci NOT NULL,
  `number` varchar(11) COLLATE utf8_polish_ci NOT NULL DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_polish_ci;

--
-- Zrzut danych tabeli `contacts`
--

INSERT INTO `contacts` (`name`, `surname`, `number`) VALUES
('Adam', 'Kowalski', 'AK'),
('Daniel', 'Potulny', 'DP'),
('Jan', 'Jaworz', 'JJ'),
('Krystian', 'Kluska', 'KK'),
('Karol', 'Zaburski', 'KZ'),
('Michal', 'Nowak', 'MN'),
('Piotr', 'Rosa', 'PR'),
('Robert', 'Majewski', 'RM'),
('Staszek', 'Bochenek', 'SB'),
('Zosia', 'Kostka', 'ZK');

--
-- Indeksy dla zrzutów tabel
--

--
-- Indexes for table `contacts`
--
ALTER TABLE `contacts`
  ADD PRIMARY KEY (`number`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
