#!/bin/perl

use strict;
use warnings;
 
use Data::Faker;
 
package Data::Faker::MyExtras;
use base qw(Data::Faker);
use Data::Faker::DateTime;
__PACKAGE__->register_plugin(
  my_short_date => sub { Data::Faker::DateTime::timestr('[%e/%b/%G:%T %z]') },
);


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

my $faker = Data::Faker->new();

my @methods = ("GET","POST");
my @codes = (200,400,401,403,404,500,500,502,503);

open INFILE, "uaPool.txt";
my @UAs;
while(my $line = <INFILE> ){
	$line =~ s/\n//g;
	$line =~ s/\r//g;
	push( @UAs, $line );
}
close INFILE;

open OUTFILE,'>',$ofile;


my $ct = $linecount;
my $brokePercent = 20;
my $randLimit = 100/$brokePercent;

my $progStep = $linecount/10;
my $notif = $progStep;

while( $ct ){

	my $method = $methods[ rand @methods ];
	my $path = "PATH";
	my $standard = "HTTP/1.1";
	my $code = $codes[ rand @codes ];
	my $bytes = int( rand(8192) );
	my $httpdPath = "URL";
	my $userAgent = $UAs[ rand @UAs ];

	my $str = $faker->ip_address." - - ".$faker->my_short_date." \"".$method." ".$path." ".$standard."\" ".$code." ".$bytes." \"".$httpdPath."\" \"".$userAgent."\"";
	if( int( rand($randLimit) ) < 1){
		$str = reverse($str);
	}
	$str = $str."\n";

	print OUTFILE $str;
	$ct--;
	if($ct > $notif){
		print "Generated ".int($notif)."/".$linecount."...\n";
		$notif = $notif + $progStep;
	}
}

close OUTFILE;

