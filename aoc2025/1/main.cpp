#include <iostream>
#include <fstream>
#include <cstring>
#include <cmath>

int parseNum(std::string &line)
{
    int num = 0;
    for (int i = line.length() - 1, j = 1; i > 0; i--, j *= 10)
    {
        num += (line[i] - '0') * j;
    }
    return num;
}

int part1(std::istream& f) {
    std::string line;

    int dial = 50, res = 0;

    while (f >> line)
    {
        int num = parseNum(line);
        switch (line[0])
        {
        case 'L':
            dial -= num;
            break;
        case 'R':
            dial += num;
            break;
        default:
            std::cerr << "Abort" << std::endl;
            return 0;
        }
        if (dial % 100 == 0)
        {
            res++;
        }
    }
    return res;
}

int part2(std::istream& f) {
    std::string line;

    int prev_dial = 50, dial = 50, res = 0;

    while (f >> line)
    {
        int num = parseNum(line);
        switch (line[0])
        {
        case 'L':
            dial -= num;
            break;
        case 'R':
            dial += num;
            break;
        default:
            std::cerr << "Abort" << std::endl;
            return 0;
        }
        if((prev_dial < 0 && dial >= 0) || (prev_dial > 0 && dial <= 0)) {
            res++;
        }
        res += abs(dial / 100);
        dial %= 100;
        prev_dial = dial;
    }
    return res;
}

int main()
{
    std::ifstream f("input.txt");

    if (!f)
    {
        std::cerr << "Error opening file" << std::endl;
        return 0;
    }

    std::cout << part1(f);
    std::cout << part2(f);

    return 0;
}