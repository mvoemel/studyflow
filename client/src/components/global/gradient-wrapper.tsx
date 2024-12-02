type GradientWrapperProps = {
  children: React.ReactNode;
  className?: string;
  wrapperClassName?: string;
};

const GradientWrapper = ({
  children,
  className,
  wrapperClassName,
}: GradientWrapperProps) => (
  <div className={`relative ${className || ""}`}>
    <div
      className={`absolute m-auto blur-[160px] ${wrapperClassName || ""}`}
      style={{
        background:
          "linear-gradient(180deg, #1C4ED8 0%, rgba(65, 128, 246, 1) 0.01%, rgba(65, 128, 246, 0.2) 100%)",
      }}
    ></div>
    <div className="relative">{children}</div>
  </div>
);

export { GradientWrapper };
