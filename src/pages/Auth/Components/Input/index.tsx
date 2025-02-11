import React from "react";
import { InputContainer, InputWrapper, Label, StyledInput } from "./styles";
import { Star } from "@/pages/SetHomeLocation/components/BottomModal/styles";

interface CustomInputProps {
  label: string;
  value: string;
  onChange: (e: React.ChangeEvent<HTMLInputElement>) => void;
  placeholder?: string;
  type?: string;
  starNeed?: boolean;
}

export default function CustomInput({
  label,
  value,
  onChange,
  placeholder = "",
  type = "text",
  starNeed = true,
}: CustomInputProps) {
  return (
    <InputWrapper>
      <Label>
        {label} {starNeed && <Star>*</Star>}
      </Label>
      <InputContainer>
        <StyledInput
          type={type}
          value={value}
          onChange={onChange}
          placeholder={placeholder}
        />
      </InputContainer>
    </InputWrapper>
  );
}
