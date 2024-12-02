import { LayoutEffect } from "../global/layer-effect";
import { faqsList } from "./data";

const FAQ = () => (
  <section className="mt-12">
    <div id="faq" className="custom-screen">
      <div className="max-w-xl text-center xl:mx-auto">
        <h2 className="text-4xl font-bold">Your Questions, Answered</h2>
        <p className="mt-3 text-muted-foreground">
          Everything you need to know about making the most of our platform, all
          in one place.
        </p>
      </div>
      <div className="mt-12">
        <LayoutEffect
          className="duration-1000 delay-300"
          isInviewState={{
            trueState: "opacity-1",
            falseState: "opacity-0 translate-y-12",
          }}
        >
          <ul className="space-y-8 gap-12 grid-cols-2 sm:grid sm:space-y-0 lg:grid-cols-3">
            {faqsList.map((item, idx) => (
              <li key={idx} className="space-y-3">
                <summary className="flex items-center justify-between font-semibold text-lg">
                  {item.q}
                </summary>
                <p className="text-muted-foreground leading-relaxed">
                  {item.a}
                </p>
              </li>
            ))}
          </ul>
        </LayoutEffect>
      </div>
    </div>
  </section>
);

export { FAQ };
