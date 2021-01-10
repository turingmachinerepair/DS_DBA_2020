#!/bin/perl

use strict;
use warnings;

my ($linecount, $percent, $ofile) = @ARGV;

if( not defined $linecount){
	print "Undefined line count.\n";
	die;
}


if( not defined $percent){
	$percent = 5;
}

if( not defined $ofile){
	$ofile = "output.txt";
}

if( $percent<0 || $percent >100){
	print "Invalid error percentage.\n";
	die;
}

if( $linecount<0){
	print "Negative line count.\n";
	die;
}

print "Generating ".$linecount." lines.\n";
print "Printing output to: ". $ofile."\n";
print "Percentage of broken records: ".$percent."%\n";

open OUTFILE,'>',$ofile;


my $ct = $linecount;
my $brokePercent = 20;
my $randLimit = 100/$brokePercent;

my $progStep = $linecount/10;
my $notif = $progStep;

while( $ct ){

	my $metricID = int( rand(8192) );
	my $timestamp = 1510670916247 + int( rand(1000000) );
	my $value =  int( rand(8192) );

	my $str = $metricID.",".$timestamp.",".$value;
	if( int( rand($randLimit) ) < 1){
		$str = "This is corrupted string placeholder.";
	}
	$str = $str."\n";

	print OUTFILE $str;
	$ct--;
	if($ct >= $notif){
		print "Generated ".int($notif)."/".$linecount."...\n";
		$notif = $notif + $progStep;
	}
}

close OUTFILE;
