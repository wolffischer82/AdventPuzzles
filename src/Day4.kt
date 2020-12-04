class PassportCollection(filename : String)
{
    val passportCollection = mutableListOf("");
    init {
        val input = readFile(filename);
        for (i in input)
        {
            if (i == "")
            {
                passportCollection.add("");
            }
            else
            {
                var currentPassport = passportCollection[passportCollection.size-1];
                currentPassport = currentPassport.plus(" $i");
                passportCollection[passportCollection.size-1] = currentPassport;
            }
        }
        println("Total passports is ${passportCollection.size}");

        var correctPassports = 0;


        for (p in passportCollection)
        {
            var byr = false;
            var iyr = false;
            var eyr = false;
            var hgt = false;
            var hcl = false;
            var ecl = false;
            var pid = false;
            println("Passport $p");

            val splitPassport = p.split(" ");

            for (attr in splitPassport) {
                val splitAttribute = attr.split(":");
                when {
                    splitAttribute[0] == "byr" -> {
                        val num = splitAttribute[1].toInt();
                        if (num in 1920..2002) {
                            byr = true;
                        }
                    }

                    splitAttribute[0] == "iyr" -> {
                        val num = splitAttribute[1].toInt();
                        if (num in 2010..2020) {
                            iyr = true;
                        }
                    }

                    splitAttribute[0] == "eyr" -> {
                        val num = splitAttribute[1].toInt();
                        if (num in 2020..2030) {
                            eyr = true;
                        }
                    }

                    splitAttribute[0] == "hgt" -> {
                        if (splitAttribute[1].endsWith("in")) {
                            val num = splitAttribute[1].removeSuffix("in").toInt();
                            if (num in 59..76) {
                                hgt = true;
                            }
                        }
                        else {
                            val num = splitAttribute[1].removeSuffix("cm").toInt();
                            if (num in 150..193) {
                                hgt = true;
                            }
                        }
                    }

                    splitAttribute[0] == "hcl" -> {
                        if (splitAttribute[1].startsWith("#")) {
                            if (splitAttribute[1].substring(1).matches(Regex("^[a-f0-9]{6}\$"))) {
                                hcl = true;
                            }
                        }
                    }
                    splitAttribute[0] == "ecl" -> {
                        if (splitAttribute[1].matches(Regex("^(amb|blu|brn|gry|grn|hzl|oth)"))) {
                            ecl = true;
                        }
                    }
                    splitAttribute[0] == "pid" -> {
                        if (splitAttribute[1].matches(Regex("^([0-9]{9})$"))) {
                            pid = true;
                        }
                    }
                }
            }



            if (byr && iyr && eyr && hgt && hcl && ecl && pid) {
                correctPassports++;
                println("\t is correct!");
            }
        }
        println("We found $correctPassports correct passports!");
    }
}

fun main(args : Array<String>)
{
    PassportCollection("src/Day4Input");
}