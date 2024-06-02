#include "proba.h"

uint32_t fact (uint8_t x)
{
	uint32_t factVar = 1;
	for ( uint8_t i=1; i<=x+1; i++)
	{
		factVar = factVar*i;
	}  
	return factVar;
}
