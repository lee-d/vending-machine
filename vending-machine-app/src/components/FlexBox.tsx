import React from 'react';
import styled from 'styled-components';

type ColFlexBoxProps = FlexBoxProps & {
  width?: string;
  height?: string;
};

type FlexBoxProps = {
  justifyContent?: string;
  alignItems?: string;
  margin?: string;
};

const StyledFlexBox = styled.div<FlexBoxProps>`
  display: flex;
  width: 100%;
  justify-content: ${props => props.justifyContent};
  align-items: ${props => props.alignItems};
  margin: ${props => props.margin};
`;
const StyledColFlexBox = styled.div<ColFlexBoxProps>`
  display: flex;
  flex-direction: column;
  width: ${props => (props.width ? props.width : '100%')};
  justify-content: ${props => props.justifyContent};
  margin: ${props => props.margin};
  height: ${props => (props.height ? props.height : '100%')};
  align-items: ${props => props.alignItems};
`;

export const FlexBox: React.FC<FlexBoxProps> = ({ children, justifyContent, alignItems, margin }) => {
  return (
    <StyledFlexBox justifyContent={justifyContent} alignItems={alignItems} margin={margin}>
      {children}
    </StyledFlexBox>
  );
};
export const ColFlexBox: React.FC<ColFlexBoxProps> = ({
  width,
  justifyContent,
  children,
  margin,
  height,
  alignItems,
}) => {
  return (
    <StyledColFlexBox
      justifyContent={justifyContent}
      width={width}
      margin={margin}
      height={height}
      alignItems={alignItems}
    >
      {children}
    </StyledColFlexBox>
  );
};
