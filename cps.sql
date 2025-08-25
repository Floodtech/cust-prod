-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Aug 25, 2025 at 10:48 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `cps`
--

-- --------------------------------------------------------

--
-- Table structure for table `customers`
--

CREATE TABLE `customers` (
  `businessName` text NOT NULL,
  `contactName` text NOT NULL,
  `customerEmail` varchar(45) NOT NULL,
  `customerTelNum` varchar(11) NOT NULL,
  `customerCellNum` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customers`
--

INSERT INTO `customers` (`businessName`, `contactName`, `customerEmail`, `customerTelNum`, `customerCellNum`) VALUES
('test', 'test', 'test@mail.com', '12459807654', '12679087654'),
('harrison college', 'frank worrell', 'fw@mail.com', '12464357890', '12462346785');

-- --------------------------------------------------------

--
-- Table structure for table `employees`
--

CREATE TABLE `employees` (
  `employeeName` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `employees`
--

INSERT INTO `employees` (`employeeName`) VALUES
('Jason Chandler'),
('Carl Taitt'),
('Jeremy Boyce'),
('Rachel Strickland'),
('Henry Farmer');

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `orderReferenceNumber` int(11) NOT NULL,
  `orderDate` date NOT NULL,
  `scheduledDeliveryDate` date NOT NULL,
  `employeeName` text NOT NULL,
  `orderStatus` text NOT NULL,
  `orderInstructions` longtext NOT NULL,
  `businessName` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`orderReferenceNumber`, `orderDate`, `scheduledDeliveryDate`, `employeeName`, `orderStatus`, `orderInstructions`, `businessName`) VALUES
(1, '2025-08-25', '2025-08-25', 'nnbvnvbn', 'hfhfghgf', 'bnbvnvnvb', 'harrison college');

-- --------------------------------------------------------

--
-- Table structure for table `products`
--

CREATE TABLE `products` (
  `productRefNum` int(11) NOT NULL,
  `productCode` varchar(10) NOT NULL,
  `orderReferenceNumber` int(9) NOT NULL,
  `quantity` int(11) NOT NULL,
  `description` longtext NOT NULL,
  `notes` longtext NOT NULL,
  `status` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `products`
--

INSERT INTO `products` (`productRefNum`, `productCode`, `orderReferenceNumber`, `quantity`, `description`, `notes`, `status`) VALUES
(1, '023', 202500001, 12, 'test', 'test', 'spell checked');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`orderReferenceNumber`);

--
-- Indexes for table `products`
--
ALTER TABLE `products`
  ADD PRIMARY KEY (`productRefNum`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `orderReferenceNumber` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `products`
--
ALTER TABLE `products`
  MODIFY `productRefNum` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
