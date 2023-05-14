package ru.itis.bookmytable.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonResponseEntity<T> {

    private int status;
    private T content;

}
