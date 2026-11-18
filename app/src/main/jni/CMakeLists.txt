cmake_minimum_required(VERSION 3.22.1)
project("hamster_core")

add_library(
        hamster_core
        SHARED
        main.cpp
)

find_library(
        log-lib
        log
)

target_link_libraries(
        hamster_core
        ${log-lib}
)
