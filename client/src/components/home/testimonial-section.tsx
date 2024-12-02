import { GradientWrapper } from "../global/gradient-wrapper";
import { LayoutEffect } from "../global/layer-effect";
import { testimonialsList } from "./data";

const Testimonial = () => {
  return (
    <section className="my-12">
      <div id="testimonials" className="custom-screen">
        <div className="max-w-2xl text-center md:mx-auto">
          <h2 className=" text-3xl font-semibold sm:text-4xl">
            Studyflow is loved by the best performing students around the world
          </h2>
        </div>
        <GradientWrapper
          wrapperClassName="max-w-sm h-40 top-12 inset-x-0"
          className="mt-12"
        >
          <LayoutEffect
            className="duration-1000 delay-300"
            isInviewState={{
              trueState: "opacity-1",
              falseState: "opacity-0 translate-y-12",
            }}
          >
            <ul className="grid gap-6 duration-1000 delay-300 ease-in-out sm:grid-cols-2 lg:grid-cols-3">
              {testimonialsList.map((item, idx) => (
                <li
                  key={idx}
                  className="p-4 rounded-xl border border-gray-800"
                  style={{
                    backgroundImage:
                      "radial-gradient(100% 100% at 50% 50%, rgba(65, 128, 246, 0.1) 0%, rgba(65, 128, 246, 0) 100%)",
                  }}
                >
                  <figure className="flex flex-col justify-between gap-y-6 h-full">
                    <blockquote className="">
                      <p className="">{item.quote}</p>
                    </blockquote>
                    <div className="flex items-center gap-x-4">
                      <img
                        src={item.avatar}
                        alt={item.name}
                        className="w-14 h-14 rounded-full object-cover"
                      />
                      <div>
                        <span className="block font-semibold">{item.name}</span>
                        <span className="block text-muted-foreground text-sm mt-0.5">
                          {item.title}
                        </span>
                      </div>
                    </div>
                  </figure>
                </li>
              ))}
            </ul>
          </LayoutEffect>
        </GradientWrapper>
      </div>
    </section>
  );
};

export { Testimonial };
