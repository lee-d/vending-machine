import React from 'react';
import styled from 'styled-components';

interface SpacingProps {
  width?: string;
  height?: string;
}

const StyledSpacing = styled.div<SpacingProps>`
  width: ${props => props.width};
  height: ${props => props.height};
`;

export const Spacing: React.FC<SpacingProps> = props => {
  return <StyledSpacing width={props.width} height={props.height} />;
};
