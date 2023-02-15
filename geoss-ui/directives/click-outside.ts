function closest(elem, selector) {
	while (elem && elem !== document.body) {
		if (elem && elem.matches(selector)) {
			return elem;
		}
		elem = elem.parentElement;
	}
	return null;
}

export default {
	bind(el, binding, vNode) {
		// Define Handler and cache it on the element
		const handler = e => {
			if (!vNode.context) {
				return;
			}

			// some components may have related popup item, on which we shall prevent the click outside event handler.
			const elements = e.path || (e.composedPath && e.composedPath());
			if(elements && elements.length > 0) {
				elements.unshift(e.target);
			}

			if (el.contains(e.target)) {
				return;
			}
			if(typeof binding.value !== 'function' && binding.value.excludeSelectors) {
				const selectors = binding.value.excludeSelectors.split(', ');
				for(const selector of selectors) {
					if(closest(e.target, selector)) {
						return;
					}
				}
			}

			el.__vueClickOutside__.callback(e);
		};

		// add Event Listeners
		el.__vueClickOutside__ = {
			handler,
			callback: binding.value
		};

		if(typeof binding.value !== 'function' && binding.value.excludeSelectors) {
			el.__vueClickOutside__.callback = binding.value.fn;
			el.__vueClickOutside__.selectors = binding.value.excludeSelectors;
		}

		document.addEventListener('click', handler);
	},

	update(el, binding) {
		if(typeof binding.value !== 'function' && binding.value.excludeSelectors) {
			el.__vueClickOutside__.callback = binding.value.fn;
			el.__vueClickOutside__.selectors = binding.value.excludeSelectors;
		} else {
			el.__vueClickOutside__.callback = binding.value;
		}
	},

	unbind(el, binding, vNode) {
		// Remove Event Listeners
		document.removeEventListener('click', el.__vueClickOutside__.handler);
		delete el.__vueClickOutside__;
	}
};