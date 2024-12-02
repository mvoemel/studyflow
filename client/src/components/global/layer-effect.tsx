"use client";

import { useInView } from "framer-motion";
import { cloneElement, useRef } from "react";

type LayoutEffectProps = {
  children: React.ReactElement;
  className?: string;
  isInviewState: { trueState: string; falseState: string };
};

const LayoutEffect = ({
  children,
  className,
  isInviewState: { trueState = "", falseState = "" },
}: LayoutEffectProps) => {
  const ref = useRef(null);
  const isInView = useInView(ref, { once: true });

  return cloneElement(children, {
    ref,
    className: `${children.props.className || ""} ${className || ""} ${
      isInView ? trueState : falseState
    }`,
  });
};

export { LayoutEffect };
