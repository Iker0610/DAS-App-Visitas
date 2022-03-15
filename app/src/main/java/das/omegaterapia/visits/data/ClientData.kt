package das.omegaterapia.visits.data

import das.omegaterapia.visits.model.Client
import das.omegaterapia.visits.model.Direction

val clients = mutableListOf(

    Client(
        name = "Manolo",
        surname = "Pérez Ramírez",
        phoneNum = "+34 655-145260",
        direction = Direction(
            address = "Ronda Yeray, 0, Bajo 3º",
            town = "El Batista",
            zip = "71871"
        )
    ),

    Client(
        name = "Eider",
        surname = "Mesa Rojo",
        phoneNum = "+34 614819500",
        direction = Direction(
            address = "Avenida Arroyo, 7, Ático 2º",
            town = "Hernádez del Vallès",
            zip = "96409"
        )
    ),

    Client(
        name = "Teodora",
        surname = "Teodora  Maroto",
        phoneNum = "969 003677",
        direction = Direction(
            address = "Praza Guerrero, 10, 34º D",
            town = "A Granados",
            zip = "22595"
        )
    ),

    Client(
        name = "Nagore",
        surname = "Montilla Bianca de La Torre",
        phoneNum = "+34 992 58 1888",
        direction = Direction(
            address = "Ronda Verduzco, 93, 8º 4º",
            town = "Posada Baja",
            zip = "47734"
        )
    ),

    Client(
        name = "Jose",
        surname = "San-Martin",
        phoneNum = "+34 935-368257",
        direction = Direction(
            address = "Praza Vega, 76, 44º F",
            town = "Candelaria del Puerto",
            zip = "70766"
        )
    ),

    Client(
        name = "Erik",
        surname = "Miguel",
        phoneNum = "606 14 6126",
        direction = Direction(
            address = "Ruela Jon, 207, 6º",
            town = "Meraz del Pozo",
            zip = "32056"
        )
    ),

    Client(
        name = "Anna",
        surname = "Castro",
        phoneNum = "+34 642683678",
        direction = Direction(
            address = "Rúa Nayara, 3, Bajo 0º",
            town = "Arellano del Barco",
            zip = "48111"
        )
    ),

    Client(
        name = "Adam",
        surname = "Rosas",
        phoneNum = "+34 648372563",
        direction = Direction(
            address = "Praza Peralta, 5, Ático 2º",
            town = "L' Montañez del Bages",
            zip = "43102"
        )
    ),

    Client(
        name = "Erik",
        surname = "Miguel",
        phoneNum = "606-77-3348",
        direction = Direction(
            address = "Calle Castañeda, 06, 3º",
            town = "Montero del Bages",
            zip = "94179"
        )
    ),
)