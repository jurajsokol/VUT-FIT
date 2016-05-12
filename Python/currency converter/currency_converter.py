#!/usr/bin/python

from argparse import ArgumentParser
from urllib.request import urlopen
from urllib.parse import quote
from urllib.error import URLError, HTTPError
import json
from sys import stderr

def parse_args_term():
    parser = ArgumentParser(description='Convert currencies.')
    parser.add_argument('--amount', type=float, required=True)
    parser.add_argument('--input_currency', type=str, required=True)
    parser.add_argument('--output_currency', type=str)
    return parser.parse_args()

def symbol(currency):
    symbols = {
                '$':'USD',
                '£':'GBP',
                '€':'EUR',
                '¥':'CNY',
                'руб':'RUB',
                'kr':'DKK',
                'R':'ZAR',
                'R$':'BRL',
                'Kč':'CZK',
                '₱':'PHP',
                '₩':'KRW',
                '฿':'THB',
                'Rp':'IDR',
                'лв':'BGN',
                '₪':'ILS',
                'kn':'HRK',
                'zł':'PLN',
                'Ft':'HUF',
                'lei':'RON',
                'RM':'MYR',
                }
    try:
        return symbols[currency]
    except KeyError:
        return currency
def main():
    args = parse_args_term()
    url = "http://api.fixer.io/latest?base=%s" % (symbol(args.input_currency))

    try:
        result = urlopen(url).read()
    except HTTPError:
        print('Unsupported input currency', file=stderr)
    except URLError:
        print('Internet connection error', file=stderr)
    else:
        result = json.loads(result.decode("utf-8"))
        x = {
            "input": {
                "amount": args.amount,
                "currency": symbol(args.input_currency)
            },
            "output" : {
            }
        }

    if args.output_currency:
        try:
            x["output"][symbol(args.output_currency)] = args.amount * result["rates"][symbol(args.output_currency)]
        except KeyError:
            print('Unsupported output currency', file=stderr)
            exit()
    else:
        for code, value in result["rates"].items():
            x["output"][code] = value*args.amount

    """JSON float representation hack"""
    json.encoder.FLOAT_REPR = lambda f: ("%.2f" % f)
    print(json.dumps(x, indent=4, sort_keys=True))

if __name__ == "__main__":
    main()
