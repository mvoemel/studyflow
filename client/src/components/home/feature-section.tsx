import { LayoutEffect } from "../global/layer-effect";
import { featuresList } from "./data";

const Features = () => {
  return (
    <section className="py-16 lg:py-24">
      <div id="features" className="custom-screen">
        <LayoutEffect
          className="duration-1000 delay-300"
          isInviewState={{
            trueState: "opacity-1",
            falseState: "opacity-0 translate-y-6",
          }}
        >
          <div className="max-w-xl mx-auto text-center">
            <h2 className="text-3xl font-semibold sm:text-4xl">
              Start your efficient learning experience with Studyflow
            </h2>
            <p className="mt-3 text-muted-foreground">
              From personalized study plans to grade tracking, discover the
              tools that make managing your semester simple, efficient, and
              stress-free.
            </p>
          </div>
        </LayoutEffect>
        <LayoutEffect
          className="duration-1000 delay-500"
          isInviewState={{
            trueState: "opacity-1",
            falseState: "opacity-0",
          }}
        >
          <div className="relative mt-12">
            <ul className="grid gap-8 sm:grid-cols-2 lg:grid-cols-3">
              {featuresList.map((item, idx) => (
                <li
                  key={idx}
                  className="space-y-3 p-4 rounded-xl border border-muted"
                  style={{
                    background:
                      "radial-gradient(157.73% 157.73% at 50% -29.9%, rgba(65, 128, 246, 0.2) 0%, rgba(203, 213, 225, 0) 100%)",
                  }}
                >
                  <div className="w-12 h-12 flex items-center justify-center bg-secondary rounded-lg">
                    {item.icon}
                  </div>
                  <h3 className="text-lg font-semibold">{item.title}</h3>
                  <p>{item.desc}</p>
                </li>
              ))}
            </ul>
          </div>
        </LayoutEffect>
      </div>
    </section>
  );
};

export { Features };
