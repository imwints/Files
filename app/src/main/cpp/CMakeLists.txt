cmake_minimum_required(VERSION 3.22.1)

project("filesystem")

add_library(filesystem SHARED filesystem.cc)

target_compile_options(filesystem PRIVATE -Wall -Wextra -Wpedantic -Wunused -Wshadow -Wold-style-cast)

target_link_libraries(filesystem android log)
