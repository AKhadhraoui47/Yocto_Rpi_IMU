#include "proba.h"

float combinaison ( uint8_t x, uint8_t n )
{
	float denominator = fact(x)*fact(n-x);
	float nominator = fact(n);
	return nominator/denominator;
}

float arrangement ( uint8_t x, uint8_t n )
{
	float denominator = fact(n-x);
	float nominator = fact(n);
	return nominator/denominator;
}
