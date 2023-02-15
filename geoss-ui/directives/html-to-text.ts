const getHTML = (str: string) => {
	const elem = document.createElement('div');
	elem.innerHTML = str;

	for (let c = elem.childNodes, i = c.length; i--; ) {
		if (c[i].nodeType === 1) {
			return elem;
		}
	}

	return null;
};

const removeExtraSpaces = (str: string) => {
	return str.replace(/( +)|([ \t]+)|([ \n]+)|([ \r]+)/g, ' ');
};

export default {
	bind(el: HTMLElement, binding) {
		const html = getHTML(binding.value);
		el.innerText = (html ? removeExtraSpaces(html.innerText) : binding.value);
	},

	update(el, binding) {
		const html = getHTML(binding.value);
		el.innerText = (html ? removeExtraSpaces(html.innerText) : binding.value);
	}
};